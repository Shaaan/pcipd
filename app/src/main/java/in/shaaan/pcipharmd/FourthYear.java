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

import in.shaaan.pcipharmd.databinding.ActivityFourthYearBinding;

public class FourthYear extends AppCompatActivity implements View.OnClickListener {
    private ActivityFourthYearBinding activityFourthYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFourthYearBinding = ActivityFourthYearBinding.inflate(getLayoutInflater());
        View view = activityFourthYearBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityFourthYearBinding.toolbar;
        setSupportActionBar(toolbar);
        AdUtil.loadAd(this);
        activityFourthYearBinding.layout4y.nativeCard41.setVisibility(View.GONE);
        activityFourthYearBinding.layout4y.nativeCard42.setVisibility(View.GONE);
        activityFourthYearBinding.layout4y.nativeCard43.setVisibility(View.GONE);
        activityFourthYearBinding.layout4y.tp3.setOnClickListener(this);
        activityFourthYearBinding.layout4y.toxicology.setOnClickListener(this);
        activityFourthYearBinding.layout4y.hp.setOnClickListener(this);
        activityFourthYearBinding.layout4y.cp.setOnClickListener(this);
        activityFourthYearBinding.layout4y.biopharm.setOnClickListener(this);
        activityFourthYearBinding.layout4y.biostat.setOnClickListener(this);

        FloatingActionButton fab = activityFourthYearBinding.fab;
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
        AdLoader adLoader41 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFourthYearBinding.layout4y.nativeAd41.setStyles(style);
                        activityFourthYearBinding.layout4y.nativeAd41.setNativeAd(unifiedNativeAd);
                    }
                }).build();
        adLoader41.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader42 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFourthYearBinding.layout4y.nativeAd42.setStyles(style);
                        activityFourthYearBinding.layout4y.nativeAd42.setNativeAd(unifiedNativeAd);
                    }
                }).build();
        adLoader42.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader43 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFourthYearBinding.layout4y.nativeAd43.setStyles(style);
                        activityFourthYearBinding.layout4y.nativeAd43.setNativeAd(unifiedNativeAd);
                    }
                }).build();
        adLoader43.loadAd(new AdRequest.Builder().build());

        activityFourthYearBinding.layout4y.nativeCard41.setVisibility(View.VISIBLE);
        activityFourthYearBinding.layout4y.nativeCard42.setVisibility(View.VISIBLE);
        activityFourthYearBinding.layout4y.nativeCard43.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tp3) {
            String s3 = "https://shaaan.github.io/pcipd/syllabus4/pt3";
            AdUtil.showInterAd(this, s3);
        } else if (id == R.id.toxicology) {
            String s = "https://shaaan.github.io/pcipd/syllabus4/toxicology";
            AdUtil.showInterAd(this, s);
        } else if (id == R.id.cp) {
            String s1 = "https://shaaan.github.io/pcipd/syllabus4/cp";
            AdUtil.showInterAd(this, s1);
        } else if (id == R.id.hp) {
            String s2 = "https://shaaan.github.io/pcipd/syllabus4/hosp_pharm";
            AdUtil.showInterAd(this, s2);
        } else if (id == R.id.biopharm) {
            String s4 = "https://shaaan.github.io/pcipd/syllabus4/biopharm";
            AdUtil.showInterAd(this, s4);
        } else if (id == R.id.biostat) {
            String s5 = "https://shaaan.github.io/pcipd/syllabus4/biostat";
            AdUtil.showInterAd(this, s5);
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
