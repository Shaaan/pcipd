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

import in.shaaan.pcipharmd.databinding.ActivitySecondYearBinding;

public class SecondYear extends AppCompatActivity implements View.OnClickListener {
    private ActivitySecondYearBinding activitySecondYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySecondYearBinding = ActivitySecondYearBinding.inflate(getLayoutInflater());
        View view = activitySecondYearBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activitySecondYearBinding.toolbar;
        setSupportActionBar(toolbar);
        AdUtil.loadAd(this);
        activitySecondYearBinding.layout2y.nativeCard21.setVisibility(View.GONE);
        activitySecondYearBinding.layout2y.nativeCard22.setVisibility(View.GONE);
        activitySecondYearBinding.layout2y.nativeCard23.setVisibility(View.GONE);
        activitySecondYearBinding.layout2y.cology.setOnClickListener(this);
        activitySecondYearBinding.layout2y.tp1.setOnClickListener(this);
        activitySecondYearBinding.layout2y.cognosy.setOnClickListener(this);
        activitySecondYearBinding.layout2y.micro.setOnClickListener(this);
        activitySecondYearBinding.layout2y.patho.setOnClickListener(this);
        activitySecondYearBinding.layout2y.com.setOnClickListener(this);

        FloatingActionButton fab = activitySecondYearBinding.fab;
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
        AdLoader adLoader21 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activitySecondYearBinding.layout2y.nativeAd21.setStyles(style);
                        activitySecondYearBinding.layout2y.nativeAd21.setNativeAd(nativeAd);

                    }
                })
                .build();

        AdLoader adLoader22 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activitySecondYearBinding.layout2y.nativeAd22.setStyles(style);
                        activitySecondYearBinding.layout2y.nativeAd22.setNativeAd(nativeAd);

                    }
                })
                .build();

        AdLoader adLoader23 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activitySecondYearBinding.layout2y.nativeAd23.setStyles(style);
                        activitySecondYearBinding.layout2y.nativeAd23.setNativeAd(nativeAd);

                    }
                })
                .build();

        adLoader21.loadAd(new AdRequest.Builder().build());
        adLoader22.loadAd(new AdRequest.Builder().build());
        adLoader23.loadAd(new AdRequest.Builder().build());
        activitySecondYearBinding.layout2y.nativeCard21.setVisibility(View.VISIBLE);
        activitySecondYearBinding.layout2y.nativeCard22.setVisibility(View.VISIBLE);
        activitySecondYearBinding.layout2y.nativeCard23.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.cology) {
            String s3 = "https://shaaan.github.io/pcipd/syllabus2/cology";
            AdUtil.showInterAd(this, s3);
        } else if (id == R.id.tp1) {
            String s = "https://shaaan.github.io/pcipd/syllabus2/tp1";
            AdUtil.showInterAd(this, s);
        } else if (id == R.id.micro) {
            String s1 = "https://shaaan.github.io/pcipd/syllabus2/micro";
            AdUtil.showInterAd(this, s1);
        } else if (id == R.id.cognosy) {
            String s2 = "https://shaaan.github.io/pcipd/syllabus2/cognosy";
            AdUtil.showInterAd(this, s2);
        } else if (id == R.id.patho) {
            String s4 = "https://shaaan.github.io/pcipd/syllabus2/patho";
            AdUtil.showInterAd(this, s4);
        } else if (id == R.id.com) {
            String s5 = "https://shaaan.github.io/pcipd/syllabus2/cmp";
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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, About.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
