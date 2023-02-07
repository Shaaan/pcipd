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
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;

import in.shaaan.pcipharmd.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    private ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = activityHomeBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityHomeBinding.toolbar;
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        refreshAd();

        activityHomeBinding.layoutHome.nativeCard01.setVisibility(View.GONE);
        activityHomeBinding.layoutHome.nativeCard02.setVisibility(View.GONE);
        activityHomeBinding.layoutHome.nativeCard03.setVisibility(View.GONE);
        activityHomeBinding.layoutHome.syllabus1.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus2.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus3.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus4.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus5.setOnClickListener(this);
        activityHomeBinding.layoutHome.syllabus6.setOnClickListener(this);

        FloatingActionButton fab = activityHomeBinding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Like the app? Rate it on playstore!", Snackbar.LENGTH_LONG)
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

        refreshAd();
    }

    public void refreshAd() {
//        Native Ads
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        findViewById(R.id.nativeCard_02).setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();
                        activityHomeBinding.layoutHome.nativeAd01.setStyles(style);
                        activityHomeBinding.layoutHome.nativeAd01.setNativeAd(nativeAd);
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
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
                        activityHomeBinding.layoutHome.nativeAd02.setStyles(style);
                        activityHomeBinding.layoutHome.nativeAd02.setNativeAd(nativeAd);
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
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
                        activityHomeBinding.layoutHome.nativeAd03.setStyles(style);
                        activityHomeBinding.layoutHome.nativeAd03.setNativeAd(nativeAd);
                    }
                }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build();
        adLoader2.loadAd(new AdRequest.Builder().build());

        activityHomeBinding.layoutHome.nativeCard01.setVisibility(View.VISIBLE);
        activityHomeBinding.layoutHome.nativeCard02.setVisibility(View.VISIBLE);
        activityHomeBinding.layoutHome.nativeCard03.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.syllabus1) {
            Intent intent1 = new Intent(this, FirstYear.class);
            startActivity(intent1);
        } else if (id == R.id.syllabus2) {
            Intent intent2 = new Intent(this, SecondYear.class);
            startActivity(intent2);
        } else if (id == R.id.syllabus3) {
            Intent intent3 = new Intent(this, ThirdYear.class);
            startActivity(intent3);
        } else if (id == R.id.syllabus4) {
            Intent intent4 = new Intent(this, FourthYear.class);
            startActivity(intent4);
        } else if (id == R.id.syllabus5) {
            Intent intent5 = new Intent(this, FifthYear.class);
            startActivity(intent5);
        } else if (id == R.id.syllabus6) {
            Intent intent6 = new Intent(this, SixthYear.class);
            startActivity(intent6);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, About.class));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
//        if (result != null && result.isDrawerOpen()) {
//            result.closeDrawer();
//        } else {
        super.onBackPressed();
//        }
    }
}
