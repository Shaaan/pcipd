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

import in.shaaan.pcipharmd.databinding.ActivityFourthYearBinding;
import in.shaaan.pcipharmd.databinding.ContentFourthYearBinding;

public class FourthYear extends AppCompatActivity implements View.OnClickListener {
    ContentFourthYearBinding yearBinding;
    private ActivityFourthYearBinding activityFourthYearBinding;
    AdUtil adUtil = new AdUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFourthYearBinding = ActivityFourthYearBinding.inflate(getLayoutInflater());
        setContentView(activityFourthYearBinding.getRoot());
        setSupportActionBar(activityFourthYearBinding.toolbar);
        yearBinding = activityFourthYearBinding.layout4y;
        adUtil.loadInterAd(this);

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        yearBinding.tp3.setOnClickListener(this);
        yearBinding.toxicology.setOnClickListener(this);
        yearBinding.hp.setOnClickListener(this);
        yearBinding.cp.setOnClickListener(this);
        yearBinding.biopharm.setOnClickListener(this);
        yearBinding.biostat.setOnClickListener(this);

        activityFourthYearBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
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
        loadNativeAd(this, yearBinding.nativeAd41, yearBinding.nativeCard41);
        loadNativeAd(this, yearBinding.nativeAd42, yearBinding.nativeCard42);
        loadNativeAd(this, yearBinding.nativeAd43, yearBinding.nativeCard43);
    }


    @Override
    public void onClick(View view) {
        Map<Integer, String> urlMap = new HashMap<>();
        urlMap.put(R.id.tp3, "https://shaaan.github.io/pcipd/syllabus4/pt3");
        urlMap.put(R.id.toxicology, "https://shaaan.github.io/pcipd/syllabus4/toxicology");
        urlMap.put(R.id.cp, "https://shaaan.github.io/pcipd/syllabus4/cp");
        urlMap.put(R.id.hp, "https://shaaan.github.io/pcipd/syllabus4/hosp_pharm");
        urlMap.put(R.id.biopharm, "https://shaaan.github.io/pcipd/syllabus4/biopharm");
        urlMap.put(R.id.biostat, "https://shaaan.github.io/pcipd/syllabus4/biostat");

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
}
