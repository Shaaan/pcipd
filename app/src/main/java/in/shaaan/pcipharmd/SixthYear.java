package in.shaaan.pcipharmd;

import static in.shaaan.pcipharmd.AdUtil.loadNativeAd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import in.shaaan.pcipharmd.databinding.ActivitySixthYearBinding;
import in.shaaan.pcipharmd.databinding.ContentSixthYearBinding;

public class SixthYear extends AppCompatActivity implements View.OnClickListener {
    ContentSixthYearBinding yearBinding;
    AdUtil adUtil = new AdUtil();
    NativeAdView frameLayout;
    private ActivitySixthYearBinding activitySixthYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySixthYearBinding = ActivitySixthYearBinding.inflate(getLayoutInflater());
        setContentView(activitySixthYearBinding.getRoot());
        setSupportActionBar(activitySixthYearBinding.toolbar);
        yearBinding = activitySixthYearBinding.layout6y;
        adUtil.loadInterAd(this);
        yearBinding.internActivities.setOnClickListener(this);
        yearBinding.internDocuments.setOnClickListener(this);

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        yearBinding.internActivities.setOnClickListener(this);
        yearBinding.internDocuments.setOnClickListener(this);

        activitySixthYearBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
                .setAction("RATE", v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.shaaan.pcipharmd"));
                    startActivity(intent);
                }).show());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refreshAd() {
        loadNativeAd(this, yearBinding.nativeAd61, yearBinding.nativeCard61);
        loadNativeAd(this, yearBinding.nativeAd62, yearBinding.nativeCard62);
    }

    @Override
    public void onClick(View view) {
        Map<Integer, String> urlMap = new HashMap<>();
        urlMap.put(R.id.intern_activities, "https://shaaan.github.io/pcipd/syllabus6");
        urlMap.put(R.id.intern_documents, "https://shaaan.github.io/pcipd/syllabus6_1/");

        String url = urlMap.get(view.getId());
        if (url != null) {
            adUtil.showInterAd(this, url);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdUtil.interstitialAd = null;
    }
}
