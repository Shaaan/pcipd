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
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.List;

public class AdUtil {
    private static final String AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359";
    private static final String TEST_DEVICE = "B12F232395821DBBEC08E0E9EF9C5EF7";
    private static final String NATIVE_AD_UNIT_ID = "ca-app-pub-1941738066609841/8926036161";
    private static InterstitialAd interstitialAd;
    private static boolean adIsLoading;

    public static void loadNativeAd(Context context, TemplateView nativeAdView, View nativeCard) {
        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions nativeAdOptions = new NativeAdOptions.Builder()
                .setRequestMultipleImages(false)
                .setVideoOptions(videoOptions)
                .build();


        AdLoader adLoader = new AdLoader.Builder(context, NATIVE_AD_UNIT_ID)
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
                        Log.e(String.valueOf(context), "Ad failed to load: " + loadAdError.getMessage());
                    }
                })
                .withNativeAdOptions(nativeAdOptions)
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadInterAd(Context context) {
        Log.i(TAG, "Google Mobile Ads SDK Version: " + MobileAds.getVersion());

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

//    public void nativeAdMedium(Activity activity, NativeAdView frameLayout) {
//        AdLoader.Builder builder = new AdLoader.Builder(activity, NATIVE_AD_UNIT_ID);
//
//        builder.forNativeAd(
//                nativeAd -> {
//                    if (activity.isDestroyed() || activity.isFinishing() || activity.isChangingConfigurations()) {
//                        nativeAd.destroy();
//                        return;
//                    }
//
//                    if (this.nativeAd != null) {
//                        this.nativeAd.destroy();
//                    }
//
//                    View view = activity.getLayoutInflater().inflate(R.layout.content_sixth_year, null);
//
//                    this.nativeAd = nativeAd;
//                    NativeAdView adView = view.findViewById(R.id.native_card_62);
//                    populateNativeAdView(nativeAd, adView);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(adView);
//                }
//        );
//
//        VideoOptions videoOptions =
//                new VideoOptions.Builder().setStartMuted(true).build();
//
//        NativeAdOptions adOptions = new NativeAdOptions.Builder()
//                .setVideoOptions(videoOptions)
//                .build();
//
//        builder.withNativeAdOptions(adOptions);
//
//        AdLoader adLoader =
//                builder
//                        .withAdListener(
//                                new AdListener() {
//                                    @Override
//                                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                                        String error =
//                                                String.format(
//                                                        Locale.getDefault(),
//                                                        "domain: %s, code: %d, message: %s",
//                                                        loadAdError.getDomain(),
//                                                        loadAdError.getCode(),
//                                                        loadAdError.getMessage());
//                                    }
//                                })
//                        .build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }
//
//    private AdLoader.Builder getBuilder(Activity activity, FrameLayout frameLayout) {
//        AdLoader.Builder builder = new AdLoader.Builder(activity, NATIVE_AD_UNIT_ID);
//
//        builder.forNativeAd(
//                nativeAd -> {
//                    if (activity.isDestroyed() || activity.isFinishing() || activity.isChangingConfigurations()) {
//                        nativeAd.destroy();
//                        return;
//                    }
//
//                    if (this.nativeAd != null) {
//                        this.nativeAd.destroy();
//                    }
//
//                    this.nativeAd = nativeAd;
//                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.content_sixth_year, null);
//                    populateNativeAdView(nativeAd, adView);
//                    frameLayout.removeAllViews();
//                    frameLayout.addView(adView);
//                }
//        );
//        return builder;
//    }
//
//    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
//        // Set the media view.
//        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline and mediaContent are guaranteed to be in every NativeAd.
//        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
//        Objects.requireNonNull(adView.getMediaView()).setMediaContent(nativeAd.getMediaContent());
//
//        // These assets aren't guaranteed to be in every NativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
//        } else {
//            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
//        } else {
//            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
//        } else {
//            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getPrice() == null) {
//            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);
//        } else {
//            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
//        } else {
//            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }
//
//        if (nativeAd.getStarRating() == null) {
//            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getAdvertiser() == null) {
//            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(nativeAd);
//
//    }
}
