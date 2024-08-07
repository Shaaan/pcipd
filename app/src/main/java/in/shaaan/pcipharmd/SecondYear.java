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

import in.shaaan.pcipharmd.databinding.ActivitySecondYearBinding;
import in.shaaan.pcipharmd.databinding.ContentSecondYearBinding;

public class SecondYear extends AppCompatActivity implements View.OnClickListener {
    ContentSecondYearBinding yearBinding;
    AdUtil adUtil = new AdUtil();
    private ActivitySecondYearBinding activitySecondYearBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySecondYearBinding = ActivitySecondYearBinding.inflate(getLayoutInflater());
        setContentView(activitySecondYearBinding.getRoot());
        setSupportActionBar(activitySecondYearBinding.toolbar);
        yearBinding = activitySecondYearBinding.layout2y;
        adUtil.loadInterAd(this);

        initializeUI();
        refreshAd();
    }

    private void initializeUI() {
        yearBinding.cology.setOnClickListener(this);
        yearBinding.tp1.setOnClickListener(this);
        yearBinding.cognosy.setOnClickListener(this);
        yearBinding.micro.setOnClickListener(this);
        yearBinding.patho.setOnClickListener(this);
        yearBinding.com.setOnClickListener(this);

        activitySecondYearBinding.fab.setOnClickListener(view -> Snackbar.make(view, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG)
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
        loadNativeAd(this, yearBinding.nativeAd21, yearBinding.nativeCard21);
        loadNativeAd(this, yearBinding.nativeAd22, yearBinding.nativeCard22);
        loadNativeAd(this, yearBinding.nativeAd23, yearBinding.nativeCard23);
    }

    @Override
    public void onClick(View view) {
        Map<Integer, String> urlMap = new HashMap<>();
        urlMap.put(R.id.cology, "https://shaaan.github.io/pcipd/syllabus2/cology");
        urlMap.put(R.id.tp1, "https://shaaan.github.io/pcipd/syllabus2/tp1");
        urlMap.put(R.id.micro, "https://shaaan.github.io/pcipd/syllabus2/micro");
        urlMap.put(R.id.cognosy, "https://shaaan.github.io/pcipd/syllabus2/cognosy");
        urlMap.put(R.id.patho, "https://shaaan.github.io/pcipd/syllabus2/patho");
        urlMap.put(R.id.com, "https://shaaan.github.io/pcipd/syllabus2/cmp");

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

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, About.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
