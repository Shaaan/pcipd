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

public class SecondYear extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.native_ad_21)
    TemplateView nativeAd21;
    @BindView(R.id.nativeCard_21)
    CardView nativeCard21;
    @BindView(R.id.native_ad_22)
    TemplateView nativeAd22;
    @BindView(R.id.nativeCard_22)
    CardView nativeCard22;
    @BindView(R.id.native_ad_23)
    TemplateView nativeAd23;
    @BindView(R.id.nativeCard_23)
    CardView nativeCard23;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_year);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nativeCard21.setVisibility(View.GONE);
        nativeCard22.setVisibility(View.GONE);
        nativeCard23.setVisibility(View.GONE);
        findViewById(R.id.cology).setOnClickListener(this);
        findViewById(R.id.tp1).setOnClickListener(this);
        findViewById(R.id.cognosy).setOnClickListener(this);
        findViewById(R.id.micro).setOnClickListener(this);
        findViewById(R.id.patho).setOnClickListener(this);
        findViewById(R.id.com).setOnClickListener(this);

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
        AdLoader adLoader21 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard21.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd21.setStyles(style);
                        nativeAd21.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader21.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        AdLoader adLoader22 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard22.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd22.setStyles(style);
                        nativeAd22.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader22.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        AdLoader adLoader23 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard23.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd23.setStyles(style);
                        nativeAd23.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader23.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1941738066609841/7774678359");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setImmersiveMode(true);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.cology:
                String s3 = "https://shaaan.github.io/pcipd/syllabus2/cology";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent3 = builder3.build();
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .enableUrlBarHiding()
                        .setShowTitle(true);
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            customTabsIntent3.launchUrl(SecondYear.this, Uri.parse(s3));
                        }
                    });
                } else {
                    customTabsIntent3.launchUrl(this, Uri.parse(s3));
                }
                break;
            case R.id.tp1:
                String s = "https://shaaan.github.io/pcipd/syllabus2/tp1";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .enableUrlBarHiding()
                        .setShowTitle(true);
                CustomTabsIntent customTabsIntent = builder.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            customTabsIntent.launchUrl(SecondYear.this, Uri.parse(s));
                        }
                    });
                } else {
                    customTabsIntent.launchUrl(this, Uri.parse(s));
                }
                break;
            case R.id.micro:
                String s1 = "https://shaaan.github.io/pcipd/syllabus2/micro";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .enableUrlBarHiding()
                        .setShowTitle(true);
                CustomTabsIntent customTabsIntent1 = builder1.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            customTabsIntent1.launchUrl(SecondYear.this, Uri.parse(s1));
                        }
                    });
                } else {
                    customTabsIntent1.launchUrl(this, Uri.parse(s1));
                }
                break;
            case R.id.cognosy:
                String s2 = "https://shaaan.github.io/pcipd/syllabus2/cognosy";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .enableUrlBarHiding()
                        .setShowTitle(true);
                CustomTabsIntent customTabsIntent2 = builder2.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            customTabsIntent2.launchUrl(SecondYear.this, Uri.parse(s2));
                        }
                    });
                } else {
                    customTabsIntent2.launchUrl(this, Uri.parse(s2));
                }
                break;
            case R.id.patho:
                String s4 = "https://shaaan.github.io/pcipd/syllabus2/patho";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .enableUrlBarHiding()
                        .setShowTitle(true);
                CustomTabsIntent customTabsIntent4 = builder4.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            customTabsIntent4.launchUrl(SecondYear.this, Uri.parse(s4));
                        }
                    });
                } else {
                    customTabsIntent4.launchUrl(this, Uri.parse(s4));
                }
                break;
            case R.id.com:
                String s5 = "https://shaaan.github.io/pcipd/syllabus2/cmp";
                CustomTabsIntent.Builder builder5 = new CustomTabsIntent.Builder();
                builder5.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .enableUrlBarHiding()
                        .setShowTitle(true);
                CustomTabsIntent customTabsIntent5 = builder5.build();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdClosed() {
                            super.onAdClosed();
                            customTabsIntent5.launchUrl(SecondYear.this, Uri.parse(s5));
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
