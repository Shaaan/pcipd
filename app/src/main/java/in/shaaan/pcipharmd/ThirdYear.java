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

import in.shaaan.pcipharmd.databinding.ActivityThirdYearBinding;

public class ThirdYear extends AppCompatActivity implements View.OnClickListener {
    private ActivityThirdYearBinding activityThirdYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityThirdYearBinding = ActivityThirdYearBinding.inflate(getLayoutInflater());
        View view = activityThirdYearBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityThirdYearBinding.toolbar;
        setSupportActionBar(toolbar);
        AdUtil.loadAd(this);
        activityThirdYearBinding.layout3y.nativeCard31.setVisibility(View.GONE);
        activityThirdYearBinding.layout3y.nativeCard32.setVisibility(View.GONE);
        activityThirdYearBinding.layout3y.nativeCard33.setVisibility(View.GONE);
        activityThirdYearBinding.layout3y.tp2.setOnClickListener(this);
        activityThirdYearBinding.layout3y.pharmac.setOnClickListener(this);
        activityThirdYearBinding.layout3y.formulation.setOnClickListener(this);
        activityThirdYearBinding.layout3y.analysis.setOnClickListener(this);
        activityThirdYearBinding.layout3y.juris.setOnClickListener(this);
        activityThirdYearBinding.layout3y.mchem.setOnClickListener(this);

        FloatingActionButton fab = activityThirdYearBinding.fab;
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
        AdLoader adLoader31 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityThirdYearBinding.layout3y.nativeAd31.setStyles(style);
                        activityThirdYearBinding.layout3y.nativeAd31.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader31.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader32 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityThirdYearBinding.layout3y.nativeAd32.setStyles(style);
                        activityThirdYearBinding.layout3y.nativeAd32.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader32.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader33 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityThirdYearBinding.layout3y.nativeAd33.setStyles(style);
                        activityThirdYearBinding.layout3y.nativeAd33.setNativeAd(nativeAd);
                    }
                }).build();
        adLoader33.loadAd(new AdRequest.Builder().build());

        activityThirdYearBinding.layout3y.nativeCard31.setVisibility(View.VISIBLE);
        activityThirdYearBinding.layout3y.nativeCard32.setVisibility(View.VISIBLE);
        activityThirdYearBinding.layout3y.nativeCard33.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.tp2) {
            String s = "https://shaaan.github.io/pcipd/syllabus3/tp2";
            AdUtil.showInterAd(this, s);
        } else if (id == R.id.pharmac) {
            String s1 = "https://shaaan.github.io/pcipd/syllabus3/pharmac";
            AdUtil.showInterAd(this, s1);
        } else if (id == R.id.formulation) {
            String s2 = "https://shaaan.github.io/pcipd/syllabus3/formulation";
            AdUtil.showInterAd(this, s2);
        } else if (id == R.id.analysis) {
            String s3 = "https://shaaan.github.io/pcipd/syllabus3/analysis";
            AdUtil.showInterAd(this, s3);
        } else if (id == R.id.juris) {
            String s4 = "https://shaaan.github.io/pcipd/syllabus3/juris";
            AdUtil.showInterAd(this, s4);
        } else if (id == R.id.mchem) {
            String s5 = "https://shaaan.github.io/pcipd/syllabus3/mchem";
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
