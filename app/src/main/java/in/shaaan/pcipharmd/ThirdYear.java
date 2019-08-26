package in.shaaan.pcipharmd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThirdYear extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.native_ad_31)
    TemplateView nativeAd31;
    @BindView(R.id.nativeCard_31)
    CardView nativeCard31;
    @BindView(R.id.native_ad_32)
    TemplateView nativeAd32;
    @BindView(R.id.nativeCard_32)
    CardView nativeCard32;
    @BindView(R.id.native_ad_33)
    TemplateView nativeAd33;
    @BindView(R.id.nativeCard_33)
    CardView nativeCard33;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_year);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nativeCard31.setVisibility(View.GONE);
        nativeCard32.setVisibility(View.GONE);
        nativeCard33.setVisibility(View.GONE);
        findViewById(R.id.tp2).setOnClickListener(this);
        findViewById(R.id.pharmac).setOnClickListener(this);
        findViewById(R.id.formulation).setOnClickListener(this);
        findViewById(R.id.analysis).setOnClickListener(this);
        findViewById(R.id.juris).setOnClickListener(this);
        findViewById(R.id.mchem).setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
                        .setAction("RATE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("market://details?id=in.shaaan.pcipharmd"));
                                startActivity(intent);
                            }
                        }).show();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

//
//
//        Google AdMob

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        refreshAd();
    }

    public void refreshAd() {
//
//        Banner Ads
//
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
//        mAdView.loadAd(adRequest);

//
//        Native Ads
//
        AdLoader adLoader31 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard31.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd31.setStyles(style);
                        nativeAd31.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader31.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        AdLoader adLoader32 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard32.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd32.setStyles(style);
                        nativeAd32.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader32.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        AdLoader adLoader33 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard33.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd33.setStyles(style);
                        nativeAd33.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader33.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1941738066609841/7774678359");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.tp2:
                String s = "https://shaaan.github.io/pcipd/syllabus3/tp2";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
//                            finish();
                            customTabsIntent.launchUrl(ThirdYear.this, Uri.parse(s));
                        }
                    });
                } else {
                    customTabsIntent.launchUrl(this, Uri.parse(s));
                }
                break;
            case R.id.pharmac:
                String s1 = "https://shaaan.github.io/pcipd/syllabus3/pharmac";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent1 = builder1.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
//                            finish();
                            customTabsIntent1.launchUrl(ThirdYear.this, Uri.parse(s1));
                        }
                    });
                } else {
                    customTabsIntent1.launchUrl(this, Uri.parse(s1));
                }
                break;
            case R.id.formulation:
                String s2 = "https://shaaan.github.io/pcipd/syllabus3/formulation";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent2 = builder2.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
//                            finish();
                            customTabsIntent2.launchUrl(ThirdYear.this, Uri.parse(s2));
                        }
                    });
                } else {
                    customTabsIntent2.launchUrl(this, Uri.parse(s2));
                }
                break;
            case R.id.analysis:
                String s3 = "https://shaaan.github.io/pcipd/syllabus3/analysis";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent3 = builder3.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
//                            finish();
                            customTabsIntent3.launchUrl(ThirdYear.this, Uri.parse(s3));
                        }
                    });
                } else {
                    customTabsIntent3.launchUrl(this, Uri.parse(s3));
                }
                break;
            case R.id.juris:
                String s4 = "https://shaaan.github.io/pcipd/syllabus3/juris";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent4 = builder4.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
//                            finish();
                            customTabsIntent4.launchUrl(ThirdYear.this, Uri.parse(s4));
                        }
                    });
                } else {
                    customTabsIntent4.launchUrl(this, Uri.parse(s4));
                }
                break;
            case R.id.mchem:
                String s5 = "https://shaaan.github.io/pcipd/syllabus3/mchem";
                CustomTabsIntent.Builder builder5 = new CustomTabsIntent.Builder();
                builder5.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent5 = builder5.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
//                            finish();
                            customTabsIntent5.launchUrl(ThirdYear.this, Uri.parse(s5));
                        }
                    });
                } else {
                    customTabsIntent5.launchUrl(this, Uri.parse(s5));
                }
                break;
            default:
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//            mInterstitialAd.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                    finish();
//                }
//            });
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, About.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
