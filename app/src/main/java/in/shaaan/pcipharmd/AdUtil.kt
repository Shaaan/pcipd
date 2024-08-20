package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class AdUtil {
    fun loadInterAd(context: Context?) {
        Log.i(ContentValues.TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion())

        // Request a new ad if one isn't already loaded.
        if (adIsLoading || interstitialAd != null) {
            return
        }
        adIsLoading = true

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(TEST_DEVICE))
                .build()
        )

        //        Interstitial Ads
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context!!,
            AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    Log.i("AdUtil:", loadAdError.message)
                    adIsLoading = false
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Companion.interstitialAd = interstitialAd
                    adIsLoading = false
                }
            })
    }

    fun showInterAd(context: Activity?, syllabus: String?) {
        val builder = CustomTabsIntent.Builder()
        val params = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            .build()
        builder.setDefaultColorSchemeParams(params).setUrlBarHidingEnabled(true).setShowTitle(true)
        val customTabsIntent = builder.build()

        if (interstitialAd == null) {
            customTabsIntent.launchUrl(context, Uri.parse(syllabus))
        } else {
            interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    customTabsIntent.launchUrl(context, Uri.parse(syllabus))
                    loadInterAd(context)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    customTabsIntent.launchUrl(context, Uri.parse(syllabus))
                    loadInterAd(context)
                }
            }
            interstitialAd!!.show(context)
        }
    }

    companion object {
        private const val AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359"
        private const val TEST_DEVICE = "7B67F5089B238CD99A00AC1A44CD8AA2"
        private const val NATIVE_AD_UNIT_ID = "ca-app-pub-1941738066609841/8926036161"
        var interstitialAd: InterstitialAd? = null
        private var adIsLoading = false

        fun loadNativeAd(context: Context, nativeAdView: TemplateView, nativeCard: View) {
            val videoOptions = VideoOptions.Builder()
                .setStartMuted(true)
                .build()

            val nativeAdOptions = NativeAdOptions.Builder()
                .setRequestMultipleImages(false)
                .setVideoOptions(videoOptions)
                .build()

            val adLoader = AdLoader.Builder(context, NATIVE_AD_UNIT_ID)
                .forNativeAd { nativeAd: NativeAd? ->
                    val style = NativeTemplateStyle.Builder()
                        .withSecondaryTextSize(0f)
                        .withTertiaryTextSize(0f)
                        .withCallToActionTextSize(0f)
                        .build()
                    nativeAdView.setStyles(style)
                    nativeAdView.setNativeAd(nativeAd)
                    nativeCard.visibility = View.VISIBLE
                }.withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        Log.e(context.toString(), "Ad failed to load: " + loadAdError.message)
                    }
                })
                .withNativeAdOptions(nativeAdOptions)
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }
}
