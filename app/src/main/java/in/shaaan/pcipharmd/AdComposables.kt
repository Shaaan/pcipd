// app/src/main/java/in/shaaan/pcipharmd/ui/components/AdComposables.kt (New File)

package `in`.shaaan.pcipharmd

import android.content.Context
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
fun NativeAdComposable(modifier: Modifier = Modifier, adUnitId: String) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
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
                // This block now only runs when we are sure we have an ad to show.
                AndroidView(
                    factory = { ctx ->
                        // 1. Inflate the XML layout in the factory. This is called only once.
                        LayoutInflater.from(ctx).inflate(R.layout.ad_unified, null) as NativeAdView
                    },
                    update = { adView ->
                        // 2. Populate the view in the update block. This is called when the
                        //    factory completes and whenever the state changes.
                        populateNativeAdView(state.nativeAd, adView)
                    }
                )
            }

            AdState.Failed -> {
                // Render nothing if the ad fails to load.
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
    LaunchedEffect(adUnitId) {
        loadNativeAd(context, adUnitId) { result ->
            // The result is handled cleanly in a single when block.
            adState = when (result) {
                is AdResult.Loaded -> AdState.Loaded(result.nativeAd)
                is AdResult.Failed -> AdState.Failed
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
    adView.mediaView = adView.findViewById<MediaView>(R.id.ad_media)
    adView.headlineView = adView.findViewById<TextView>(R.id.ad_headline)
    adView.bodyView = adView.findViewById<TextView>(R.id.ad_body)
    adView.callToActionView = adView.findViewById<Button>(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById<ImageView>(R.id.ad_app_icon)
    adView.starRatingView = adView.findViewById<RatingBar>(R.id.ad_stars)
    adView.advertiserView = adView.findViewById<TextView>(R.id.ad_advertiser)

    // Populate the content.
    (adView.headlineView as TextView).text = nativeAd.headline
    adView.mediaView?.mediaContent = nativeAd.mediaContent

    if (nativeAd.body == null) {
        adView.bodyView?.visibility = View.INVISIBLE
    } else {
        adView.bodyView?.visibility = View.VISIBLE
        (adView.bodyView as TextView).text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        adView.callToActionView?.visibility = View.INVISIBLE
    } else {
        adView.callToActionView?.visibility = View.VISIBLE
        (adView.callToActionView as Button).text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        adView.iconView?.visibility = View.GONE
    } else {
        (adView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
        adView.iconView?.visibility = View.VISIBLE
    }

    if (nativeAd.starRating == null) {
        adView.starRatingView?.visibility = View.INVISIBLE
    } else {
        (adView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
        adView.starRatingView?.visibility = View.VISIBLE
    }

    if (nativeAd.advertiser == null) {
        adView.advertiserView?.visibility = View.INVISIBLE
    } else {
        (adView.advertiserView as TextView).text = nativeAd.advertiser
        adView.advertiserView?.visibility = View.VISIBLE
    }

    // Register the ad object with the view. This is the crucial final step.
    adView.setNativeAd(nativeAd)
}