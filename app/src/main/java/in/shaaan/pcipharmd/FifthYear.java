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

import in.shaaan.pcipharmd.databinding.ActivityFifthYearBinding;

public class FifthYear extends AppCompatActivity implements View.OnClickListener {
    private ActivityFifthYearBinding activityFifthYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFifthYearBinding = ActivityFifthYearBinding.inflate(getLayoutInflater());
        View view = activityFifthYearBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityFifthYearBinding.toolbar;
        setSupportActionBar(toolbar);
        AdUtil.loadAd(this);
        activityFifthYearBinding.layout5y.nativeCard51.setVisibility(View.GONE);
        activityFifthYearBinding.layout5y.nativeCard52.setVisibility(View.GONE);
        activityFifthYearBinding.layout5y.epi.setOnClickListener(this);
        activityFifthYearBinding.layout5y.tdm.setOnClickListener(this);
        activityFifthYearBinding.layout5y.research.setOnClickListener(this);

        FloatingActionButton fab = activityFifthYearBinding.fab;
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
        AdLoader adLoader51 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFifthYearBinding.layout5y.nativeAd51.setStyles(style);
                        activityFifthYearBinding.layout5y.nativeAd51.setNativeAd(unifiedNativeAd);
                    }
                }).build();
        adLoader51.loadAd(new AdRequest.Builder().build());

        AdLoader adLoader52 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityFifthYearBinding.layout5y.nativeAd52.setStyles(style);
                        activityFifthYearBinding.layout5y.nativeAd52.setNativeAd(unifiedNativeAd);
                    }
                }).build();
        adLoader52.loadAd(new AdRequest.Builder().build());

        activityFifthYearBinding.layout5y.nativeCard51.setVisibility(View.VISIBLE);
        activityFifthYearBinding.layout5y.nativeCard52.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.epi) {
            String epi = "https://shaaan.github.io/pcipd/syllabus5/epi/";
            AdUtil.showInterAd(this, epi);
        } else if (id == R.id.research) {
            String s = "https://shaaan.github.io/pcipd/syllabus5/research";
            AdUtil.showInterAd(this, s);
        } else if (id == R.id.tdm) {
            String s1 = "https://shaaan.github.io/pcipd/syllabus5/tdm";
            AdUtil.showInterAd(this, s1);
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
//        AdUtil.gInterstitialAd(this);
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

    @Override
    public void onResume() {
        super.onResume();
//        checkAdLoaded();
//        AdUtil.gRefreshAd(this);
    }
}
