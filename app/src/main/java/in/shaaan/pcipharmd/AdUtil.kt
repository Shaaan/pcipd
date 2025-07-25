package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import `in`.shaaan.pcipharmd.config.RemoteConfigHelper

object AdUtil {
    // WHAT: Ad Unit IDs are now sourced from the RemoteConfigHelper.
    // WHY: To make the ad unit IDs fully dynamic and updatable from the Firebase console
    //      without needing to release a new app version.
    // HOW: By calling the getter properties on the RemoteConfigHelper object.
    private val AD_UNIT_ID get() = RemoteConfigHelper.interstitialAdUnitId
    val NATIVE_AD_UNIT_ID get() = RemoteConfigHelper.nativeAdUnitId

    private var interstitialAd: InterstitialAd? = null
    private var adIsLoading = false

    fun loadInterAd(context: Context) {
        if (adIsLoading || interstitialAd != null) {
            return
        }
        // Ensure we don't try to load an ad with an empty ID
        val adUnitId = AD_UNIT_ID
        if (adUnitId.isEmpty()) {
            Log.w("AdUtil", "Interstitial Ad Unit ID is empty. Ad not loaded.")
            return
        }
        adIsLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            adUnitId,
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

    fun showInterAd(activity: Activity, syllabusUrl: String, @ColorInt toolbarColor: Int) {
        val customTabsIntent = buildCustomTabsIntent(activity, toolbarColor)

        val ad = interstitialAd
        if (ad == null) {
            customTabsIntent.launchUrl(activity, syllabusUrl.toUri())
            loadInterAd(activity)
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                customTabsIntent.launchUrl(activity, syllabusUrl.toUri())
                interstitialAd = null
                loadInterAd(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                customTabsIntent.launchUrl(activity, syllabusUrl.toUri())
                interstitialAd = null
                loadInterAd(activity)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdUtil", "Interstitial ad showed fullscreen content.")
            }
        }
        ad.show(activity)
    }

    private fun buildCustomTabsIntent(
        context: Context,
        @ColorInt toolbarColor: Int
    ): CustomTabsIntent {
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(toolbarColor)
            .build()
        return CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(params)
            .setUrlBarHidingEnabled(true)
            .setShowTitle(true)
            .build()
    }

}
