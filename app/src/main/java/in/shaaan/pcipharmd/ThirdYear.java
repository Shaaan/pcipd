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

import in.shaaan.pcipharmd.databinding.ActivityThirdYearBinding;
import in.shaaan.pcipharmd.databinding.ContentThirdYearBinding;

public class ThirdYear extends AppCompatActivity implements View.OnClickListener {
    ContentThirdYearBinding yearBinding;
    AdUtil adUtil = new AdUtil();
    private ActivityThirdYearBinding activityThirdYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityThirdYearBinding = ActivityThirdYearBinding.inflate(getLayoutInflater());
        setContentView(activityThirdYearBinding.getRoot());
        setSupportActionBar(activityThirdYearBinding.toolbar);
        yearBinding = activityThirdYearBinding.layout3y;
        adUtil.loadInterAd(this);

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        yearBinding.tp2.setOnClickListener(this);
        yearBinding.pharmac.setOnClickListener(this);
        yearBinding.formulation.setOnClickListener(this);
        yearBinding.analysis.setOnClickListener(this);
        yearBinding.juris.setOnClickListener(this);
        yearBinding.mchem.setOnClickListener(this);

        activityThirdYearBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
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
        loadNativeAd(this, yearBinding.nativeAd31, yearBinding.nativeCard31);
        loadNativeAd(this, yearBinding.nativeAd32, yearBinding.nativeCard32);
        loadNativeAd(this, yearBinding.nativeAd33, yearBinding.nativeCard33);
    }

    @Override
    public void onClick(View view) {
        Map<Integer, String> urlMap = new HashMap<>();
        urlMap.put(R.id.tp2, "https://shaaan.github.io/pcipd/syllabus3/tp2");
        urlMap.put(R.id.pharmac, "https://shaaan.github.io/pcipd/syllabus3/pharmac");
        urlMap.put(R.id.formulation, "https://shaaan.github.io/pcipd/syllabus3/formulation");
        urlMap.put(R.id.analysis, "https://shaaan.github.io/pcipd/syllabus3/analysis");
        urlMap.put(R.id.juris, "https://shaaan.github.io/pcipd/syllabus3/juris");
        urlMap.put(R.id.mchem, "https://shaaan.github.io/pcipd/syllabus3/mchem");

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
