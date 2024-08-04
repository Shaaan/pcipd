package in.shaaan.pcipharmd;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.List;

public class AdUtil {
    private static InterstitialAd interstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359";
    private static boolean adIsLoading;
    private static final String TEST_DEVICE = "F0C2258F020EEFC5BDA4F97C7758B629";

    public void loadInterAd(Context context) {
        Log.i(TAG,"Google Mobile Ads SDK Version: " + MobileAds.getVersion());

        // Request a new ad if one isn't already loaded.
        if (adIsLoading || interstitialAd != null) {
            return;
        }
        adIsLoading = true;

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder()
                        .setTestDeviceIds(List.of(TEST_DEVICE))
                        .build());

//        MobileAds.initialize(context);

//        Interstitial Ads
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                AdUtil.interstitialAd = null;
                Log.i("AdUtil:", loadAdError.getMessage());
                adIsLoading = false;
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                AdUtil.interstitialAd = interstitialAd;
                adIsLoading = false;
            }
        });
    }

    public void showInterAd(Activity context, String syllabus) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build();
        builder.setDefaultColorSchemeParams(params).setUrlBarHidingEnabled(true).setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();

        if (interstitialAd == null) {
            customTabsIntent.launchUrl(context, Uri.parse(syllabus));
        } else {
            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    customTabsIntent.launchUrl(context, Uri.parse(syllabus));
                    loadInterAd(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    customTabsIntent.launchUrl(context, Uri.parse(syllabus));
                    loadInterAd(context);
                }
            });
            interstitialAd.show(context);
        }
    }

    public static void loadNativeAd(Context context, TemplateView nativeAdView, View nativeCard, AdRequest adRequest) {
        NativeAdOptions nativeAdOptions = new NativeAdOptions.Builder()
                .setRequestMultipleImages(true)
                .build();

        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(nativeAd -> {
                    NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                            .withSecondaryTextSize(0)
                            .withTertiaryTextSize(0)
                            .withCallToActionTextSize(0)
                            .build();
                    nativeAdView.setStyles(style);
                    nativeAdView.setNativeAd(nativeAd);
                    nativeCard.setVisibility(View.VISIBLE);
                }).withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.e("HomeActivity", "Ad failed to load: " + loadAdError.getMessage());
                    }
                })
                .withNativeAdOptions(nativeAdOptions)
                .build();
        adLoader.loadAds(adRequest, 5);
    }
}
