package com.spotify.heroic.aggregation.simple;

import static com.spotify.heroic.aggregation.simple.DistributionPointUtils.*;



import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spotify.heroic.aggregation.AggregationInstance;
import com.spotify.heroic.aggregation.AggregationOutput;
import com.spotify.heroic.aggregation.AggregationSession;
import com.spotify.heroic.aggregation.ChainInstance;
import com.spotify.heroic.aggregation.EmptyInstance;
import com.spotify.heroic.aggregation.GroupInstance;
import com.spotify.heroic.aggregation.GroupingAggregation;
import com.spotify.heroic.common.DateRange;
import com.spotify.heroic.common.Series;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class FilterTdigestAggregationTest {
   private static final  double [] data1 = {0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1,0.1};
   private static final  double [] data3 = {0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3,0.3};
   private static final  double [] data4 = {0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4,0.4};
   private static final long timestamp = 1;

    @Test
    public void testFilterKAreaSession() {
        final GroupingAggregation g =
            new GroupInstance(Optional.of(ImmutableList.of("site")), EmptyInstance.INSTANCE);

        double [] quantiles = {0.5,0.75,0.99};

        double [] data = {};

        final AggregationInstance b1 = ChainInstance.of(g, new TdigestStatInstance(2, 1, quantiles ));

        final Set<Series> series = new HashSet<>();
        final Series s1 = Series.of("foo", ImmutableMap.of("site", "s1", "host", "h1"));
        final Series s2 = Series.of("foo", ImmutableMap.of("site", "s2" , "host", "h2"));
        final Series s3 = Series.of("foo", ImmutableMap.of("site", "s3",  "host", "h3" ));
        final Series s4 = Series.of("foo", ImmutableMap.of("site", "s4",  "host", "h4"));

        series.add(s1);
        series.add(s2);
        series.add(s3);
        //series.add(s4);

        final AggregationSession session = b1.session(new DateRange(0, 10000));

        session.updateDistributionPoints(s1.getTags(), series,
            ImmutableList.of(createDistributionPoint(data1,timestamp),createDistributionPoint(data1,timestamp+ 1) ));
        session.updateDistributionPoints(s2.getTags(), series,
            ImmutableList.of(createDistributionPoint(data1,timestamp),createDistributionPoint(data1,timestamp + 1)));
        session.updateDistributionPoints(s3.getTags(), series,
            ImmutableList.of(createDistributionPoint(data3,timestamp), createDistributionPoint(data3,timestamp + 2)));

        final List<AggregationOutput> result = session.result().getResult();

        System.out.println("Result " + result.size());

        //assertEquals(1, result.size());

        //AggregationOutput first = result.get(0);

        System.out.println(result);

//        if (first.getKey().equals(ImmutableMap.of("site", "lon"))) {
//            assertEquals(ImmutableList.of(new Point(1, 3.0), new Point(2, 3.0)),
//                first.getMetrics().data());
//        } else {
//            Assert.fail("unexpected group: " + first.getKey());
//        }
    }
}
