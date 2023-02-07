package in.shaaan.pcipharmd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import in.shaaan.pcipharmd.databinding.ActivityFirstYearBinding;

public class FirstYear extends AppCompatActivity implements View.OnClickListener {
    private ActivityFirstYearBinding activityFirstYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFirstYearBinding = ActivityFirstYearBinding.inflate(getLayoutInflater());
        View view = activityFirstYearBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityFirstYearBinding.toolbar;
        setSupportActionBar(toolbar);
        AdUtil.loadAd(this);
        activityFirstYearBinding.layout1y.nativeCard11.setVisibility(View.GONE);
        activityFirstYearBinding.layout1y.nativeCard12.setVisibility(View.GONE);
        activityFirstYearBinding.layout1y.nativeCard13.setVisibility(View.GONE);
        activityFirstYearBinding.layout1y.hap.setOnClickListener(this);
        activityFirstYearBinding.layout1y.ceutics.setOnClickListener(this);
        activityFirstYearBinding.layout1y.biochem.setOnClickListener(this);
        activityFirstYearBinding.layout1y.oc.setOnClickListener(this);
        activityFirstYearBinding.layout1y.ic.setOnClickListener(this);
        activityFirstYearBinding.layout1y.remMathBio.setOnClickListener(this);

        FloatingActionButton fab = activityFirstYearBinding.fab;
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

        refreshAd();
    }

    public void refreshAd() {
//      Native Ads
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFirstYearBinding.layout1y.nativeAd11.setStyles(style);
                        activityFirstYearBinding.layout1y.nativeAd11.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader1 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFirstYearBinding.layout1y.nativeAd12.setStyles(style);
                        activityFirstYearBinding.layout1y.nativeAd12.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader1.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader2 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFirstYearBinding.layout1y.nativeAd13.setStyles(style);
                        activityFirstYearBinding.layout1y.nativeAd13.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader2.loadAd(new AdRequest.Builder().build());

        activityFirstYearBinding.layout1y.nativeCard11.setVisibility(View.VISIBLE);
        activityFirstYearBinding.layout1y.nativeCard12.setVisibility(View.VISIBLE);
        activityFirstYearBinding.layout1y.nativeCard13.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.hap) {
            String hap = "https://shaaan.github.io/pcipd/syllabus1/hap/";
            AdUtil.showInterAd(this, hap);
        } else if (id == R.id.ceutics) {
            String ceutics = "https://shaaan.github.io/pcipd/syllabus1/pahrmaceutics/";
            AdUtil.showInterAd(this, ceutics);
        } else if (id == R.id.biochem) {
            String biochem = "https://shaaan.github.io/pcipd/syllabus1/biochem/";
            AdUtil.showInterAd(this, biochem);
        } else if (id == R.id.ic) {
            String ic = "https://shaaan.github.io/pcipd/syllabus1/ic/";
            AdUtil.showInterAd(this, ic);
        } else if (id == R.id.oc) {
            String s = "https://shaaan.github.io/pcipd/syllabus1/oc/";
            AdUtil.showInterAd(this, s);
        } else if (id == R.id.rem_mathBio) {
            String mb = "https://shaaan.github.io/pcipd/syllabus1/math_bio/";
            AdUtil.showInterAd(this, mb);
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
