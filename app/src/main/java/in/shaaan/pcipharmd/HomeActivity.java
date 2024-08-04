package in.shaaan.pcipharmd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import in.shaaan.pcipharmd.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityHomeBinding activityHomeBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        setSupportActionBar(activityHomeBinding.toolbar);
        FirebaseAnalytics.getInstance(this);

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        activityHomeBinding.layoutHome.syllabus1.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus2.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus3.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus4.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus5.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus6.setOnClickListener(this);

        activityHomeBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
                .setAction("RATE", v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.shaaan.pcipharmd"));
                    startActivity(intent);
                }).show());
    }

    public void refreshAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        loadNativeAd(activityHomeBinding.layoutHome.nativeAd01, activityHomeBinding.layoutHome.nativeCard01, adRequest);
        loadNativeAd(activityHomeBinding.layoutHome.nativeAd02, activityHomeBinding.layoutHome.nativeCard02, adRequest);
        loadNativeAd(activityHomeBinding.layoutHome.nativeAd03, activityHomeBinding.layoutHome.nativeCard03, adRequest);
    }

    private void loadNativeAd(TemplateView nativeAdView, View nativeCard, AdRequest adRequest) {
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
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
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(adRequest);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        Class<?> activityClass = switch (id) {
            case R.id.syllabus1 -> FirstYear.class;
            case R.id.syllabus2 -> SecondYear.class;
            case R.id.syllabus3 -> ThirdYear.class;
            case R.id.syllabus4 -> FourthYear.class;
            case R.id.syllabus5 -> FifthYear.class;
            case R.id.syllabus6 -> SixthYear.class;
            default -> null;
        };

        if (activityClass != null) {
            startActivity(new Intent(this, activityClass));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
