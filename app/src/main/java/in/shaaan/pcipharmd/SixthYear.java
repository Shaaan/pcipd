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

import in.shaaan.pcipharmd.databinding.ActivitySixthYearBinding;

public class SixthYear extends AppCompatActivity implements View.OnClickListener {
    private ActivitySixthYearBinding activitySixthYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySixthYearBinding = ActivitySixthYearBinding.inflate(getLayoutInflater());
        View view = activitySixthYearBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activitySixthYearBinding.toolbar;
        setSupportActionBar(toolbar);
        AdUtil.loadAd(this);
        activitySixthYearBinding.layout6y.nativeCard61.setVisibility(View.GONE);
        activitySixthYearBinding.layout6y.internActivities.setOnClickListener(this);
        activitySixthYearBinding.layout6y.internDocuments.setOnClickListener(this);

        FloatingActionButton fab = activitySixthYearBinding.fab;
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
//        Native Ads
        AdLoader adLoader61 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd unifiedNativeAd) {
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activitySixthYearBinding.layout6y.nativeAd61.setStyles(style);
                        activitySixthYearBinding.layout6y.nativeAd61.setNativeAd(unifiedNativeAd);
                        activitySixthYearBinding.layout6y.nativeCard61.setVisibility(View.VISIBLE);
                    }
                }).build();
        adLoader61.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.intern_activities) {
            String s = "https://shaaan.github.io/pcipd/syllabus6";
            AdUtil.showInterAd(this, s);
        } else if (id == R.id.intern_documents) {
            String s1 = "https://shaaan.github.io/pcipd/syllabus6_1/";
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
