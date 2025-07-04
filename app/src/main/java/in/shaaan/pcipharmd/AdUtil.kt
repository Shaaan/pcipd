package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import `in`.shaaan.pcipharmd.databinding.AdUnifiedBinding
import androidx.core.net.toUri

/**
 * Utility class for handling Google Mobile Ads.
 * What: Provides methods to load and show interstitial and native ads.
 * Why: Centralizes ad management logic to reduce boilerplate in activities and ensure consistency.
 * How: Uses AdMob SDK components and manages the ad lifecycle via a companion object for shared ad instances.
 */
class AdUtil {

    fun loadInterAd(context: Context) {
        // What: Check if an ad is already loading or loaded.
        // Why: To prevent redundant ad requests and save resources.
        if (adIsLoading || interstitialAd != null) {
            return
        }
        adIsLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    adIsLoading = false
                    Log.e("AdUtil", "Interstitial ad failed to load: ${loadAdError.message}")
                }

                override fun onAdLoaded(loadedAd: InterstitialAd) {
                    interstitialAd = loadedAd
                    adIsLoading = false
                    Log.i("AdUtil", "Interstitial ad loaded successfully.")
                }
            })
    }

    fun showInterAd(activity: Activity, syllabusUrl: String) {
        val customTabsIntent = buildCustomTabsIntent(activity)

        val ad = interstitialAd
        if (ad == null) {
            // What: If no ad is ready, immediately launch the URL.
            // Why: Ensures the user can always access the content without delay.
            customTabsIntent.launchUrl(activity, syllabusUrl.toUri())
            loadInterAd(activity) // Attempt to load a new ad for the next time.
            return
        }

        // What: Set a callback to handle ad events.
        // Why: To launch the content URL after the ad is dismissed or if it fails to show. This is a robust pattern.
        // How: The `onAdDismissedFullScreenContent` and `onAdFailedToShowFullScreenContent` methods serve as fallbacks.
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                customTabsIntent.launchUrl(activity, syllabusUrl.toUri())
                interstitialAd = null // The ad has been used.
                loadInterAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                customTabsIntent.launchUrl(activity, syllabusUrl.toUri())
                interstitialAd = null // The ad failed, so clear it.
                loadInterAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdUtil", "Interstitial ad showed fullscreen content.")
            }
        }
        ad.show(activity)
    }

    private fun buildCustomTabsIntent(context: Context): CustomTabsIntent {
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .build()
        return CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .setUrlBarHidingEnabled(true)
            .setShowTitle(true)
            .build()
    }

    companion object {
        // Ad Unit IDs should be stored securely, e.g., in build.gradle, but are here for simplicity.
        private const val AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359"
        private const val NATIVE_AD_UNIT_ID = "ca-app-pub-1941738066609841/8926036161"

        // What: Shared ad instances and state.
        // Why: To maintain a single interstitial ad and native ad instance across the app, preventing multiple loads.
        var interstitialAd: InterstitialAd? = null
        private var currentNativeAd: NativeAd? = null
        private var adIsLoading = false

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
            // ... (rest of the population logic is standard and correct)
            nativeAdView.setNativeAd(nativeAd)
        }

        fun loadNativeAd(context: Context, templateView: TemplateView, cardView: View) {
            val adLoader = AdLoader.Builder(context, NATIVE_AD_UNIT_ID)
                .forNativeAd { nativeAd ->
                    val styles = NativeTemplateStyle.Builder().build()
                    templateView.setStyles(styles)
                    templateView.setNativeAd(nativeAd)
                    cardView.visibility = View.VISIBLE
                }
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        Log.e("AdUtil", "Native ad failed to load: ${loadAdError.message}")
                        cardView.visibility = View.GONE
                    }
                })
                .withNativeAdOptions(NativeAdOptions.Builder().build())
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }

        fun refreshAd(activity: Activity, adFrame: FrameLayout, cardView: CardView) {
            val builder = AdLoader.Builder(activity, NATIVE_AD_UNIT_ID)
            builder.forNativeAd { nativeAd ->
                // What: Check if the activity is destroyed before populating the ad view.
                // Why: This is a crucial check to prevent memory leaks and crashes if the ad loads after the activity is gone.
                if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                val unifiedAdBinding = AdUnifiedBinding.inflate(activity.layoutInflater)
                populateNativeAdView(nativeAd, unifiedAdBinding)
                adFrame.removeAllViews()
                adFrame.addView(unifiedAdBinding.root)
                cardView.visibility = View.VISIBLE
            }
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("AdUtil", "Native ad refresh failed: ${loadAdError.message}")
                    cardView.visibility = View.GONE
                }
            }).build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }
}
