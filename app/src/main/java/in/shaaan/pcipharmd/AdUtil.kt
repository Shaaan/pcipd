package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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

fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

object AdUtil {
    private val AD_UNIT_ID get() = RemoteConfigHelper.interstitialAdUnitId
    val NATIVE_AD_UNIT_ID get() = RemoteConfigHelper.nativeAdUnitId

    private var interstitialAd: InterstitialAd? = null
    private var adIsLoading = false

    private val ALLOWED_HOSTS = setOf("pcipd.hypertex.co.in", "pci.gov.in", "pcipd-34920.firebaseio.com")

    private fun isValidSyllabusUrl(url: String): Boolean {
        return try {
            val uri = url.toUri()
            val scheme = uri.scheme
            val host = uri.host
            (scheme == "http" || scheme == "https") && (host != null && ALLOWED_HOSTS.any { host.endsWith(it) })
        } catch (_: Exception) {
            false
        }
    }

    private fun launchValidatedUrl(activity: Activity, url: String, customTabsIntent: CustomTabsIntent) {
        if (isValidSyllabusUrl(url)) {
            customTabsIntent.launchUrl(activity, url.toUri())
        } else {
            Log.e("AdUtil", "Blocked opening untrusted URL: $url")
        }
    }

    fun loadInterAd(context: Context) {
        if (adIsLoading || interstitialAd != null) {
            return
        }
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
            }
        )
    }

    fun showInterAd(context: Context, syllabusUrl: String, @ColorInt toolbarColor: Int) {
        val activity = context.findActivity()
        val customTabsIntent = buildCustomTabsIntent(toolbarColor)

        if (activity == null) {
            Log.e("AdUtil", "Could not resolve Activity from context for showing ad.")
            return
        }

        val ad = interstitialAd
        if (ad == null) {
            launchValidatedUrl(activity, syllabusUrl, customTabsIntent)
            loadInterAd(context)
            return
        }

        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                launchValidatedUrl(activity, syllabusUrl, customTabsIntent)
                interstitialAd = null
                loadInterAd(context)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                launchValidatedUrl(activity, syllabusUrl, customTabsIntent)
                interstitialAd = null
                loadInterAd(context)
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("AdUtil", "Interstitial ad showed fullscreen content.")
            }
        }
        ad.show(activity)
    }

    private fun buildCustomTabsIntent(@ColorInt toolbarColor: Int): CustomTabsIntent {
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
