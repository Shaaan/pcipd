// app/src/main/java/in/shaaan/pcipharmd/ui/components/AdComposables.kt (New File)

package `in`.shaaan.pcipharmd.ui.components

import android.view.View
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import `in`.shaaan.pcipharmd.databinding.AdUnifiedBinding

@Composable
fun NativeAdComposable(modifier: Modifier = Modifier, adUnitId: String) {
    var nativeAd by remember { mutableStateOf<NativeAd?>(null) }
    var adView by remember { mutableStateOf<NativeAdView?>(null) }
    var isAdVisible by remember { mutableStateOf(false) }

    // What: Explicitly provide the generic type <AdUnifiedBinding>.
    // Why: To resolve the compiler's type inference failure.
    // How: Add `<AdUnifiedBinding>` directly after the function name.
    AndroidViewBinding<AdUnifiedBinding>(
        factory = { inflater, parent, attachToParent ->
            AdUnifiedBinding.inflate(inflater, parent, attachToParent).also {
                adView = it.root
            }
        },
        modifier = modifier.fillMaxWidth(),
        update = { binding: AdUnifiedBinding ->
            // Now the compiler correctly knows that 'binding' is of type AdUnifiedBinding.
            nativeAd?.let { ad ->
                populateNativeAdView(ad, binding)
                binding.root.visibility = View.VISIBLE
                isAdVisible = true
            }
            if (!isAdVisible) {
                binding.root.visibility = View.GONE
            }
        } as AdUnifiedBinding.() -> Unit
    )

    LaunchedEffect(adUnitId) {
        // This part remains the same
        val adLoader = AdLoader.Builder(adView!!.context, adUnitId)
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    isAdVisible = false
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
}

// What: This helper function was moved from AdUtil.
// Why: Its logic is tightly coupled to the ad view binding, so it makes sense to co-locate it with the composable that uses it.
// How: It takes the ad object and the view binding and maps the ad assets to the corresponding views.
private fun populateNativeAdView(nativeAd: NativeAd, unifiedAdBinding: AdUnifiedBinding) {
    val nativeAdView = unifiedAdBinding.root
    nativeAdView.mediaView = unifiedAdBinding.adMedia
    nativeAdView.headlineView = unifiedAdBinding.adHeadline
    nativeAdView.bodyView = unifiedAdBinding.adBody
    nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
    nativeAdView.iconView = unifiedAdBinding.adAppIcon
    unifiedAdBinding.adHeadline.text = nativeAd.headline
    nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.mediaContent = it }
    unifiedAdBinding.adBody.visibility = if (nativeAd.body != null) View.VISIBLE else View.INVISIBLE
    unifiedAdBinding.adBody.text = nativeAd.body

    if (nativeAd.callToAction == null) {
        unifiedAdBinding.adCallToAction.visibility = View.INVISIBLE
    } else {
        unifiedAdBinding.adCallToAction.visibility = View.VISIBLE
        unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        unifiedAdBinding.adAppIcon.visibility = View.GONE
    } else {
        unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
        unifiedAdBinding.adAppIcon.visibility = View.VISIBLE
    }

    nativeAdView.setNativeAd(nativeAd)
}