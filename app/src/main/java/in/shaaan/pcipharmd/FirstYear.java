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

public class FirstYear extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.native_ad_11)
    TemplateView nativeAd11;
    @BindView(R.id.nativeCard_11)
    CardView nativeCard11;
    @BindView(R.id.native_ad_12)
    TemplateView nativeAd12;
    @BindView(R.id.nativeCard_12)
    CardView nativeCard12;
    @BindView(R.id.native_ad_13)
    TemplateView nativeAd13;
    @BindView(R.id.nativeCard_13)
    CardView nativeCard13;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_year);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nativeCard11.setVisibility(View.GONE);
        nativeCard12.setVisibility(View.GONE);
        nativeCard13.setVisibility(View.GONE);
        findViewById(R.id.hap).setOnClickListener(this);
        findViewById(R.id.ceutics).setOnClickListener(this);
        findViewById(R.id.biochem).setOnClickListener(this);
        findViewById(R.id.oc).setOnClickListener(this);
        findViewById(R.id.ic).setOnClickListener(this);
        findViewById(R.id.rem_mathBio).setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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

//        Interstitial Ads
//
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-1941738066609841/7774678359");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("010E297A73E360936A053C01A2D8902F").build());

//        Native Ads
//
        AdLoader adLoader11 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard11.setVisibility(View.VISIBLE);
                        nativeCard12.setVisibility(View.VISIBLE);
                        nativeCard13.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd11.setStyles(style);
                        nativeAd11.setNativeAd(unifiedNativeAd);
                        nativeAd12.setNativeAd(unifiedNativeAd);
                        nativeAd13.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader11.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        /*AdLoader adLoader12 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard12.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd12.setStyles(style);
                        nativeAd12.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader12.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        AdLoader adLoader13 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard13.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        nativeAd13.setStyles(style);
                        nativeAd13.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader13.loadAd(new AdRequest.Builder().addTestDevice("010E297A73E360936A053C01A2D8902F").build());
*/
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.hap:
                String hap = "https://shaaan.github.io/pcipd/syllabus1/hap/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(hap));
                break;
            case R.id.ceutics:
                String ceutics = "https://shaaan.github.io/pcipd/syllabus1/pahrmaceutics/";
                CustomTabsIntent.Builder builderC = new CustomTabsIntent.Builder();
                builderC.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntentC = builderC.build();
                customTabsIntentC.launchUrl(this, Uri.parse(ceutics));
                break;
            case R.id.biochem:
                String biochem = "https://shaaan.github.io/pcipd/syllabus1/biochem/";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(biochem));
                break;
            case R.id.ic:
                String ic = "https://shaaan.github.io/pcipd/syllabus1/ic/";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(ic));
                break;
            case R.id.oc:
                String s = "https://shaaan.github.io/pcipd/syllabus1/oc/";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s));
                break;
            case R.id.rem_mathBio:
                String mb = "https://shaaan.github.io/pcipd/syllabus1/math_bio/";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent4 = builder4.build();
                customTabsIntent4.launchUrl(this, Uri.parse(mb));
            default:
        }
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }
            });
        } else {
            super.onResume();
        }
    }

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
