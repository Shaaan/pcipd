package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.cardview.widget.CardView
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
import `in`.shaaan.pcipharmd.databinding.AdUnifiedBinding

/**
 * Utility class for handling Google Mobile Ads.
 * What: Provides methods to load and show interstitial and native ads.
 * Why: Centralizes ad management logic to reduce boilerplate in activities and ensure consistency.
 * How: Uses AdMob SDK components and manages the ad lifecycle via a singleton object for shared ad instances.
 */
// --- FIX 1: CONVERT TO SINGLETON OBJECT ---
// What: Converted the `class AdUtil` with a `companion object` into a singleton `object AdUtil`.
// Why: The previous design was flawed, mixing instance methods with static state, which could lead to
//      race conditions and confusion. A singleton `object` correctly represents that the ad state
//      (like `interstitialAd`) is shared across the entire application. This ensures a single,
//      consistent source of truth for ad management.
// How: Changed the `class` keyword to `object` and moved all properties and methods into the object's body.
object AdUtil {
    private const val AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359"
    private const val NATIVE_AD_UNIT_ID = "ca-app-pub-1941738066609841/8926036161"

    private var interstitialAd: InterstitialAd? = null
    private var adIsLoading = false

    // --- FIX 2: REMOVE UNNECESSARY NATIVE AD STATE ---
    // What: Removed the `currentNativeAd` property.
    // Why: This property was used to hold a reference to the last loaded native ad, creating a potential
    //      race condition when multiple ads were requested simultaneously. It is not needed for memory
    //      management, as the `NativeAd` object's lifecycle is tied to the view it's populated into.
    //      Removing it simplifies the logic and eliminates the risk of state corruption.
    // How: The `currentNativeAd` variable and any code that assigned to or destroyed it were deleted.
    // private var currentNativeAd: NativeAd? = null

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

    private fun populateNativeAdView(nativeAd: NativeAd, unifiedAdBinding: AdUnifiedBinding) {
        val nativeAdView = unifiedAdBinding.root
        nativeAdView.mediaView = unifiedAdBinding.adMedia
        nativeAdView.headlineView = unifiedAdBinding.adHeadline
        nativeAdView.bodyView = unifiedAdBinding.adBody
        nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
        nativeAdView.iconView = unifiedAdBinding.adAppIcon
        unifiedAdBinding.adHeadline.text = nativeAd.headline
        nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.mediaContent = it }
        unifiedAdBinding.adBody.visibility =
            if (nativeAd.body != null) View.VISIBLE else View.INVISIBLE
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

    fun refreshAd(activity: Activity, adFrame: FrameLayout, cardView: CardView) {
        val builder = AdLoader.Builder(activity, NATIVE_AD_UNIT_ID)
        builder.forNativeAd { nativeAd ->
            if (activity.isDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                nativeAd.destroy()
                return@forNativeAd
            }
            // The `currentNativeAd` logic was removed from here.
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