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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

public class FirstYear extends AppCompatActivity implements View.OnClickListener{

    private AdView mAdView;
    private NativeExpressAdView nativeExpressAdView;
    private NativeExpressAdView nativeExpressAdView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.nativeCard_21).setVisibility(View.GONE);
        findViewById(R.id.nativeCard_22).setVisibility(View.GONE);
        findViewById(R.id.hap).setOnClickListener(this);
        findViewById(R.id.ceutics).setOnClickListener(this);
        findViewById(R.id.biochem).setOnClickListener(this);
        findViewById(R.id.oc).setOnClickListener(this);
        findViewById(R.id.ic).setOnClickListener(this);
        MobileAds.initialize(this, "ca-app-pub-1941738066609841~7536308276");
        nativeExpressAdView = (NativeExpressAdView) findViewById(R.id.advert_21);
        nativeExpressAdView1 = (NativeExpressAdView) findViewById(R.id.advert_22);
        AdRequest request = new AdRequest.Builder().addTestDevice("83292CF42ABC0992E918B70ED66AFCCB").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
        AdRequest request1 = new AdRequest.Builder().addTestDevice("83292CF42ABC0992E918B70ED66AFCCB").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
        nativeExpressAdView.loadAd(request);
        nativeExpressAdView1.loadAd(request1);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
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
        nativeExpressAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                findViewById(R.id.nativeCard_21).setVisibility(View.VISIBLE);
                findViewById(R.id.nativeCard_22).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.hap:
                String hap = "https://shaaan.github.io/pcipd/syllabus1/hap/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(hap));
                break;
            case R.id.ceutics:
                String ceutics = "https://shaaan.github.io/pcipd/syllabus1/pahrmaceutics/";
                CustomTabsIntent.Builder builderC = new CustomTabsIntent.Builder();
                builderC.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntentC = builderC.build();
                customTabsIntentC.launchUrl(this, Uri.parse(ceutics));
                break;
            case R.id.biochem:
                String biochem = "https://shaaan.github.io/pcipd/syllabus1/biochem/";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(biochem));
                break;
            case R.id.ic:
                String ic = "https://shaaan.github.io/pcipd/syllabus1/ic/";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(ic));
                break;
            case R.id.oc:
                String s = "https://shaaan.github.io/pcipd/syllabus1/oc/";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s));
                break;
            default:
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
