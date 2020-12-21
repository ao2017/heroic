package com.spotify.heroic.aggregation.simple

import com.spotify.heroic.aggregation.AggregationContext
import com.spotify.heroic.aggregation.SamplingAggregation
import com.spotify.heroic.aggregation.SamplingQuery
import com.spotify.heroic.common.Duration

<<<<<<< HEAD:aggregation/simple/src/main/java/com/spotify/heroic/aggregation/simple/Tdigest.kt
data class Tdigest(
=======
/**
 * TDigest distribution point aggregation module.
 * As the name indicates, this module supports distribution point built
 * * with tDigest data sketches.
 *
 *  @author adeleo
 */
data class TdigestStat(
>>>>>>> adele/distribution:aggregation/simple/src/main/java/com/spotify/heroic/aggregation/simple/TdigestStat.kt
        val sampling: SamplingQuery?,
        override var size: Duration?,
        override var extent: Duration?
) : SamplingAggregation {

    init {
        size = size ?: sampling?.size
        extent = extent ?: sampling?.extent
    }

<<<<<<< HEAD:aggregation/simple/src/main/java/com/spotify/heroic/aggregation/simple/Tdigest.kt
    override fun apply(context: AggregationContext?, size: Long, extent: Long): TdigestInstance {
        return TdigestInstance(size, extent)
    }

    companion object {
        const val NAME = "tdigest"
=======
    override fun apply(context: AggregationContext?, size: Long, extent: Long): TdigestStatInstance {
        val quantiles :DoubleArray = DEFAULT_QUANTILES
        return TdigestStatInstance(size, extent, quantiles )
    }

    companion object {
        const val NAME = "tdigeststat"
         val DEFAULT_QUANTILES  = doubleArrayOf(0.5,0.75,0.99)
>>>>>>> adele/distribution:aggregation/simple/src/main/java/com/spotify/heroic/aggregation/simple/TdigestStat.kt
    }
}