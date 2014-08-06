package com.spotify.heroic.metrics;

import java.util.Collection;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.spotify.heroic.async.Callback;
import com.spotify.heroic.injection.Lifecycle;
import com.spotify.heroic.metrics.model.FetchDataPoints;
import com.spotify.heroic.model.DateRange;
import com.spotify.heroic.model.TimeSerie;
import com.spotify.heroic.model.WriteEntry;
import com.spotify.heroic.model.WriteResponse;
import com.spotify.heroic.statistics.MetricBackendReporter;
import com.spotify.heroic.yaml.ValidationException;

public interface MetricBackend extends Lifecycle {
    public abstract class YAML {
        private static final double DEFAULT_THRESHOLD = 10.0;
        private static final long DEFAULT_COOLDOWN = 60000;

        @Getter
        @Setter
        private double threshold = DEFAULT_THRESHOLD;

        @Getter
        @Setter
        private long cooldown = DEFAULT_COOLDOWN;

        public MetricBackend build(String context,
                MetricBackendReporter reporter) throws ValidationException {
            final MetricBackend delegate = buildDelegate(context, reporter);
            return new DisablingMetricBackend(delegate, threshold, cooldown);
        }

        protected abstract MetricBackend buildDelegate(String context,
                MetricBackendReporter reporter) throws ValidationException;
    }

    /**
     * Execute a single write.
     *
     * @param write
     * @return
     */
    public Callback<WriteResponse> write(WriteEntry write);

    /**
     * Write a collection of datapoints for a specific time series.
     *
     * @param timeSerie
     *            Time serie to write to.
     * @param data
     *            Datapoints to write.
     * @return A callback indicating if the write was successful or not.
     */
    public Callback<WriteResponse> write(Collection<WriteEntry> writes);

    /**
     * Query for data points that is part of the specified list of rows and
     * range.
     *
     * @param query
     *            The query for fetching data points. The query contains rows
     *            and a specified time range.
     *
     * @return A list of asynchronous data handlers for the resulting data
     *         points. This is suitable to use with GroupQuery. There will be
     *         one query per row.
     *
     * @throws QueryException
     */
    public List<Callback<FetchDataPoints.Result>> query(
            final TimeSerie timeSerie, final DateRange range);

    /**
     * Gets the total number of columns that are in the given rows
     *
     * @param rows
     * @return
     */
    public Callback<Long> getColumnCount(final TimeSerie timeSerie,
            DateRange range);

    @Override
    public boolean isReady();
}
