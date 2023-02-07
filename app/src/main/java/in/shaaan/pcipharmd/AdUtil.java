package in.shaaan.pcipharmd;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Arrays;
import java.util.List;

public class AdUtil {
    private static InterstitialAd interstitialAd;
    private static final String AD_UNIT_ID = "ca-app-pub-1941738066609841/7774678359";

    public static void loadAd(Context context) {

        List<String> testDeviceIds = Arrays.asList("F0C2258F020EEFC5BDA4F97C7758B629");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(context);

//        Interstitial Ads
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(context, AD_UNIT_ID, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.i("AdUtil:", loadAdError.getMessage());
            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                AdUtil.interstitialAd = interstitialAd;
            }
        });
    }

    public static void showInterAd(Activity context, String syllabus) {

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
                    loadAd(context);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    customTabsIntent.launchUrl(context, Uri.parse(syllabus));
                    loadAd(context);
                }
            });
            interstitialAd.show(context);
        }
    }
}
