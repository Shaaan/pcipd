package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

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

        private fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
            adView.mediaView = adView.findViewById(R.id.ad_media)
            adView.headlineView = adView.findViewById(R.id.ad_headline)
            adView.bodyView = adView.findViewById(R.id.ad_body)
            adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
            adView.iconView = adView.findViewById(R.id.ad_app_icon)

            (adView.headlineView as? TextView)?.text = nativeAd.headline
            adView.mediaView?.mediaContent = nativeAd.mediaContent

            if (nativeAd.body == null) {
                adView.bodyView?.visibility = View.INVISIBLE
            } else {
                adView.bodyView?.visibility = View.VISIBLE
                (adView.bodyView as? TextView)?.text = nativeAd.body
            }

            if (nativeAd.callToAction == null) {
                adView.callToActionView?.visibility = View.INVISIBLE
            } else {
                adView.callToActionView?.visibility = View.VISIBLE
                (adView.callToActionView as? TextView)?.text = nativeAd.callToAction
            }

            if (nativeAd.icon == null) {
                adView.iconView?.visibility = View.GONE
            } else {
                (adView.iconView as? ImageView)?.setImageDrawable(nativeAd.icon?.drawable)

            }

            adView.setNativeAd(nativeAd)
        }

        fun loadNativeAd(activity: Activity, adFrame: FrameLayout, cardView: View) {
            val builder = AdLoader.Builder(activity, NATIVE_AD_UNIT_ID)
                .forNativeAd { nativeAd ->
                    if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                        nativeAd.destroy()
                        return@forNativeAd
                    }
                    currentNativeAd?.destroy()
                    currentNativeAd = nativeAd

                    val adView = activity.layoutInflater.inflate(
                        R.layout.ad_unified_custom,
                        null
                    ) as NativeAdView
                    populateNativeAdView(nativeAd, adView)
                    adFrame.removeAllViews()
                    adFrame.addView(adView)
                    cardView.visibility = View.VISIBLE
                }
            val adLoader = builder.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("AdUtil", "Native ad failed to load: ${loadAdError.message}")
                    cardView.visibility = View.GONE
                }
            }).withNativeAdOptions(NativeAdOptions.Builder().build()).build()

            adLoader.loadAd(AdRequest.Builder().build())
        }
    }
}
