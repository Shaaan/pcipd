package in.shaaan.pcipharmd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class FourthYear extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.nativeCard_51).setVisibility(View.GONE);
        findViewById(R.id.nativeCard_52).setVisibility(View.GONE);
        findViewById(R.id.tp3).setOnClickListener(this);
        findViewById(R.id.toxicology).setOnClickListener(this);
        findViewById(R.id.hp).setOnClickListener(this);
        findViewById(R.id.cp).setOnClickListener(this);
        findViewById(R.id.biopharm).setOnClickListener(this);
        findViewById(R.id.biostat).setOnClickListener(this);

        MobileAds.initialize(this, "ca-app-pub-1941738066609841~7536308276");
        AdView mAdView = (AdView) findViewById(R.id.adView4);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("83292CF42ABC0992E918B70ED66AFCCB").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
        mAdView.loadAd(adRequest);
        /*NativeExpressAdView nativeExpressAdView = (NativeExpressAdView) findViewById(R.id.advert_51);
        NativeExpressAdView nativeExpressAdView1 = (NativeExpressAdView) findViewById(R.id.advert_52);
        AdRequest request = new AdRequest.Builder().addTestDevice("83292CF42ABC0992E918B70ED66AFCCB").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
        AdRequest request1 = new AdRequest.Builder().addTestDevice("83292CF42ABC0992E918B70ED66AFCCB").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
        nativeExpressAdView.loadAd(request);
        nativeExpressAdView1.loadAd(request1);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        /*nativeExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                findViewById(R.id.nativeCard_51).setVisibility(View.VISIBLE);
                findViewById(R.id.nativeCard_52).setVisibility(View.VISIBLE);
            }
        });*/
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.tp3:
                String s3 = "https://shaaan.github.io/pcipd/syllabus4/pt3";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s3));
                break;
            case R.id.toxicology:
                String s = "https://shaaan.github.io/pcipd/syllabus4/toxicology";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(s));
                break;
            case R.id.cp:
                String s1 = "https://shaaan.github.io/pcipd/syllabus4/cp";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(s1));
                break;
            case R.id.hp:
                String s2 = "https://shaaan.github.io/pcipd/syllabus4/hosp_pharm";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(s2));
                break;
            case R.id.biopharm:
                String s4 = "https://shaaan.github.io/pcipd/syllabus4/biopharm";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent4 = builder4.build();
                customTabsIntent4.launchUrl(this, Uri.parse(s4));
                break;
            case R.id.biostat:
                String s5 = "https://shaaan.github.io/pcipd/syllabus4/biostat";
                CustomTabsIntent.Builder builder5 = new CustomTabsIntent.Builder();
                builder5.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent5 = builder5.build();
                customTabsIntent5.launchUrl(this, Uri.parse(s5));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
