package in.shaaan.pcipharmd;

import static in.shaaan.pcipharmd.AdUtil.loadNativeAd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import in.shaaan.pcipharmd.databinding.ActivityFirstYearBinding;
import in.shaaan.pcipharmd.databinding.ContentFirstYearBinding;

public class FirstYear extends AppCompatActivity implements View.OnClickListener {
    ContentFirstYearBinding yearBinding;
    AdUtil adUtil = new AdUtil();
    private ActivityFirstYearBinding activityFirstYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFirstYearBinding = ActivityFirstYearBinding.inflate(getLayoutInflater());
        setContentView(activityFirstYearBinding.getRoot());
        setSupportActionBar(activityFirstYearBinding.toolbar);
        yearBinding = activityFirstYearBinding.layout1y;
        adUtil.loadInterAd(this);

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        yearBinding.hap.setOnClickListener(this);
        yearBinding.ceutics.setOnClickListener(this);
        yearBinding.biochem.setOnClickListener(this);
        yearBinding.oc.setOnClickListener(this);
        yearBinding.ic.setOnClickListener(this);
        yearBinding.remMathBio.setOnClickListener(this);

        activityFirstYearBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
                .setAction("RATE", v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.shaaan.pcipharmd"));
                    startActivity(intent);
                }).show());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refreshAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        loadNativeAd(this, yearBinding.nativeAd11, yearBinding.nativeCard11);
        loadNativeAd(this, yearBinding.nativeAd12, yearBinding.nativeCard12);
        loadNativeAd(this, yearBinding.nativeAd13, yearBinding.nativeCard13);
    }

    @Override
    public void onClick(View view) {
        Map<Integer, String> urlMap = new HashMap<>();
        urlMap.put(R.id.hap, "https://shaaan.github.io/pcipd/syllabus1/hap/");
        urlMap.put(R.id.ceutics, "https://shaaan.github.io/pcipd/syllabus1/pahrmaceutics/");
        urlMap.put(R.id.biochem, "https://shaaan.github.io/pcipd/syllabus1/biochem/");
        urlMap.put(R.id.ic, "https://shaaan.github.io/pcipd/syllabus1/ic/");
        urlMap.put(R.id.oc, "https://shaaan.github.io/pcipd/syllabus1/oc/");
        urlMap.put(R.id.rem_mathBio, "https://shaaan.github.io/pcipd/syllabus1/math_bio/");

        String url = urlMap.get(view.getId());
        if (url != null) {
            adUtil.showInterAd(this, url);
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
