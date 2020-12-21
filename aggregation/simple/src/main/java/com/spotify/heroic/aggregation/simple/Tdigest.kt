package com.spotify.heroic.aggregation.simple

import com.spotify.heroic.aggregation.AggregationContext
import com.spotify.heroic.aggregation.SamplingAggregation
import com.spotify.heroic.aggregation.SamplingQuery
import com.spotify.heroic.common.Duration

data class Tdigest(
        val sampling: SamplingQuery?,
        override var size: Duration?,
        override var extent: Duration?
) : SamplingAggregation {

    init {
        size = size ?: sampling?.size
        extent = extent ?: sampling?.extent
    }

    override fun apply(context: AggregationContext?, size: Long, extent: Long): TdigestInstance {
        return TdigestInstance(size, extent)
    }

    companion object {
        const val NAME = "tdigest"
    }
}