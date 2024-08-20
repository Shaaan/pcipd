package `in`.shaaan.pcipharmd

import android.app.Activity
import android.content.ContentValues
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
import `in`.shaaan.pcipharmd.databinding.AdUnifiedBinding

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
        private const val TEST_DEVICE = "7B67F5089B238CD99A00AC1A44CD8AA"
        private const val NATIVE_AD_UNIT_ID = "ca-app-pub-1941738066609841/8926036161"
        private var currentNativeAd: NativeAd? = null
        var interstitialAd: InterstitialAd? = null
        private var adIsLoading = false

        private fun populateNativeAdView(
            nativeAd: NativeAd,
            unifiedAdBinding: AdUnifiedBinding
        ) {
            val nativeAdView = unifiedAdBinding.root

            // Set the media view.
            nativeAdView.mediaView = unifiedAdBinding.adMedia

            // Set other ad assets.
            nativeAdView.headlineView = unifiedAdBinding.adHeadline
            nativeAdView.bodyView = unifiedAdBinding.adBody
            nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
            nativeAdView.iconView = unifiedAdBinding.adAppIcon
            nativeAdView.priceView = unifiedAdBinding.adPrice
            nativeAdView.starRatingView = unifiedAdBinding.adStars
            nativeAdView.storeView = unifiedAdBinding.adStore
            nativeAdView.advertiserView = unifiedAdBinding.adAdvertiser

            // The headline and media content are guaranteed to be in every UnifiedNativeAd.
            unifiedAdBinding.adHeadline.text = nativeAd.headline
            nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.mediaContent = it }

            // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
            // check before trying to display them.
            if (nativeAd.body == null) {
                unifiedAdBinding.adBody.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adBody.visibility = View.VISIBLE
                unifiedAdBinding.adBody.text = nativeAd.body
            }

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

            if (nativeAd.price == null) {
                unifiedAdBinding.adPrice.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adPrice.visibility = View.VISIBLE
                unifiedAdBinding.adPrice.text = nativeAd.price
            }

            if (nativeAd.store == null) {
                unifiedAdBinding.adStore.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adStore.visibility = View.VISIBLE
                unifiedAdBinding.adStore.text = nativeAd.store
            }

            if (nativeAd.starRating == null) {
                unifiedAdBinding.adStars.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adStars.rating = nativeAd.starRating!!.toFloat()
                unifiedAdBinding.adStars.visibility = View.VISIBLE
            }

            if (nativeAd.advertiser == null) {
                unifiedAdBinding.adAdvertiser.visibility = View.INVISIBLE
            } else {
                unifiedAdBinding.adAdvertiser.text = nativeAd.advertiser
                unifiedAdBinding.adAdvertiser.visibility = View.VISIBLE
            }

            // This method tells the Google Mobile Ads SDK that you have finished populating your
            // native ad view with this native ad.
            nativeAdView.setNativeAd(nativeAd)
        }

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

        fun refreshAd(activity: Activity, adFrame: FrameLayout, nativeCard62: CardView) {

            val builder = AdLoader.Builder(activity, NATIVE_AD_UNIT_ID)

            builder.forNativeAd { nativeAd ->
                // OnUnifiedNativeAdLoadedListener implementation.
                // If this callback occurs after the activity is destroyed, you must call
                // destroy and return or you may get a memory leak.
                val activityDestroyed: Boolean = activity.isDestroyed
                if (activityDestroyed) {
                    nativeAd.destroy()
                    return@forNativeAd
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                val unifiedAdBinding = AdUnifiedBinding.inflate(activity.layoutInflater)
                populateNativeAdView(nativeAd, unifiedAdBinding)
                adFrame.removeAllViews()
                adFrame.addView(unifiedAdBinding.root)
                nativeCard62.visibility = View.VISIBLE
            }

            val adOptions = NativeAdOptions.Builder().build()

            builder.withNativeAdOptions(adOptions)

            val adLoader =
                builder
                    .withAdListener(
                        object : AdListener() {
                            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                                Log.e(this.toString(), loadAdError.message)
                            }
                        }
                    )
                    .build()

            adLoader.loadAd(AdRequest.Builder().build())

        }
    }
}
