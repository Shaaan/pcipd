package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

/**
 * Utility class for handling Google Mobile Ads.
 * What: Provides methods to load and show interstitial and native ads.
 * Why: Centralizes ad management logic to reduce boilerplate in activities and ensure consistency.
 * How: Uses AdMob SDK components and manages the ad lifecycle via a singleton object for shared ad instances.
 */
object AdUtil {
    private const val AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359"
    const val NATIVE_AD_UNIT_ID = "ca-app-pub-1941738066609841/8926036161"

    private var interstitialAd: InterstitialAd? = null
    private var adIsLoading = false

    fun loadInterAd(context: Context) {
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

}