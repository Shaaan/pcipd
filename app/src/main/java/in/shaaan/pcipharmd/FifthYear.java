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

import in.shaaan.pcipharmd.databinding.ActivityFifthYearBinding;
import in.shaaan.pcipharmd.databinding.ContentFifthYearBinding;

public class FifthYear extends AppCompatActivity implements View.OnClickListener {
    private ActivityFifthYearBinding activityFifthYearBinding;
    ContentFifthYearBinding yearBinding;
    AdUtil adUtil = new AdUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFifthYearBinding = ActivityFifthYearBinding.inflate(getLayoutInflater());
        setContentView(activityFifthYearBinding.getRoot());
        setSupportActionBar(activityFifthYearBinding.toolbar);
        yearBinding = activityFifthYearBinding.layout5y;
        adUtil.loadInterAd(this);

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        yearBinding.nativeCard51.setVisibility(View.GONE);
        yearBinding.nativeCard52.setVisibility(View.GONE);
        yearBinding.epi.setOnClickListener(this);
        yearBinding.tdm.setOnClickListener(this);
        yearBinding.research.setOnClickListener(this);

        activityFifthYearBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
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
        loadNativeAd(this, yearBinding.nativeAd51, yearBinding.nativeCard51, adRequest);
        loadNativeAd(this, yearBinding.nativeAd52, yearBinding.nativeCard52, adRequest);
        loadNativeAd(this, yearBinding.nativeAd53, yearBinding.nativeCard53, adRequest);
    }

    @Override
    public void onClick(View view) {
        Map<Integer, String> urlMap = new HashMap<>();
        urlMap.put(R.id.epi, "https://shaaan.github.io/pcipd/syllabus5/epi");
        urlMap.put(R.id.research, "https://shaaan.github.io/pcipd/syllabus5/research");
        urlMap.put(R.id.tdm, "https://shaaan.github.io/pcipd/syllabus5/tdm");

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
    public void onResume() {
        super.onResume();
//        checkAdLoaded();
//        AdUtil.gRefreshAd(this);
    }
}
