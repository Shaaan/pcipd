package `in`.shaaan.pcipharmd

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.valentinilk.shimmer.shimmer

private sealed class AdResult {
    data class Loaded(val nativeAd: NativeAd) : AdResult()
    data object Failed : AdResult()
}

@Composable
fun NativeAdComposable(
    modifier: Modifier = Modifier,
    adUnitId: String,
    onAdFailed: () -> Unit = {}
) {
    var adState by remember { mutableStateOf<AdState>(AdState.Loading) }
    val context = LocalContext.current

    DisposableEffect(adState) {
        onDispose {
            if (adState is AdState.Loaded) {
                (adState as AdState.Loaded).nativeAd.destroy()
            }
        }
    }

    AnimatedContent(
        targetState = adState,
        label = "AdStateAnimation",
        modifier = modifier
    ) { state ->
        when (state) {
            is AdState.Loading -> ShimmerAdPlaceholder()
            is AdState.Loaded -> {
                AndroidView(
                    factory = { ctx ->
                        LayoutInflater.from(ctx).inflate(R.layout.ad_unified_custom, null) as NativeAdView
                    },
                    update = { adView ->
                        populateNativeAdView(state.nativeAd, adView)
                    }
                )
            }

            AdState.Failed -> {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }

    LaunchedEffect(adUnitId) {
        loadNativeAd(context, adUnitId) { result ->
            adState = when (result) {
                is AdResult.Loaded -> AdState.Loaded(result.nativeAd)
                is AdResult.Failed -> {
                    onAdFailed()
                    AdState.Failed
                }
            }
        }
    }
}

private fun loadNativeAd(
    context: Context,
    adUnitId: String,
    onResult: (AdResult) -> Unit
) {
    val adLoader = AdLoader.Builder(context, adUnitId)
        .forNativeAd { ad: NativeAd ->
            onResult(AdResult.Loaded(ad))
        }
        .withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                onResult(AdResult.Failed)
            }
        })
        .build()
    adLoader.loadAd(AdRequest.Builder().build())
}

private sealed class AdState {
    data object Loading : AdState()
    data class Loaded(val nativeAd: NativeAd) : AdState()
    data object Failed : AdState()
}

@Composable
private fun ShimmerAdPlaceholder() {
    Box(modifier = Modifier.height(280.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .shimmer(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.7f)
                        .background(Color.LightGray)
                )
            }
        }
    }
}

private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
    try {
        adView.mediaView = adView.findViewById(R.id.ad_media)
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)
        adView.starRatingView = adView.findViewById(R.id.ad_stars)
        adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

        // Headline
        (adView.headlineView as? TextView)?.text = nativeAd.headline

        // Media
        adView.mediaView?.mediaContent = nativeAd.mediaContent

        // Body
        val bodyView = adView.bodyView as? TextView
        if (bodyView != null) {
            if (nativeAd.body == null) {
                bodyView.visibility = View.INVISIBLE
            } else {
                bodyView.visibility = View.VISIBLE
                bodyView.text = nativeAd.body
            }
        }

        // Call to Action
        val ctaView = adView.callToActionView as? Button
        if (ctaView != null) {
            if (nativeAd.callToAction == null) {
                ctaView.visibility = View.INVISIBLE
            } else {
                ctaView.visibility = View.VISIBLE
                ctaView.text = nativeAd.callToAction
            }
        }

        // App Icon
        val iconView = adView.iconView as? ImageView
        if (iconView != null) {
            if (nativeAd.icon == null) {
                iconView.visibility = View.GONE
            } else {
                iconView.setImageDrawable(nativeAd.icon?.drawable)
                iconView.visibility = View.VISIBLE
            }
        }

        // Star Rating (Safely check if view exists in XML)
        val starRatingView = adView.starRatingView as? RatingBar
        if (starRatingView != null) {
            if (nativeAd.starRating == null) {
                starRatingView.visibility = View.INVISIBLE
            } else {
                starRatingView.rating = nativeAd.starRating!!.toFloat()
                starRatingView.visibility = View.VISIBLE
            }
        }

        // Advertiser (Safely check if view exists in XML)
        val advertiserView = adView.advertiserView as? TextView
        if (advertiserView != null) {
            if (nativeAd.advertiser == null) {
                advertiserView.visibility = View.INVISIBLE
            } else {
                advertiserView.text = nativeAd.advertiser
                advertiserView.visibility = View.VISIBLE
            }
        }

        // Register native ad
        adView.setNativeAd(nativeAd)
    } catch (e: Exception) {
        Log.e("AdComposables", "Error populating native ad view", e)
    }
}
