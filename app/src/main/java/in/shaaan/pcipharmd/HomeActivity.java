package in.shaaan.pcipharmd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.syllabus1).setOnClickListener(this);
        findViewById(R.id.syllabus2).setOnClickListener(this);
        findViewById(R.id.syllabus3).setOnClickListener(this);
        findViewById(R.id.syllabus4).setOnClickListener(this);
        findViewById(R.id.syllabus5).setOnClickListener(this);
        findViewById(R.id.syllabus6).setOnClickListener(this);
        MobileAds.initialize(this, "ca-app-pub-1941738066609841~7536308276");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
        mAdView.loadAd(adRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.syllabus1:
                Intent intent1 = new Intent(this, FirstYear.class);
                startActivity(intent1);
                break;
            case R.id.syllabus2:
                Intent intent2 = new Intent(this, SecondYear.class);
                startActivity(intent2);
                break;
            case R.id.syllabus3:
                Intent intent3 = new Intent(this, ThirdYear.class);
                startActivity(intent3);
                break;
            case R.id.syllabus4:
                Intent intent4 = new Intent(this, FourthYear.class);
                startActivity(intent4);
                break;
            case R.id.syllabus5:
                Intent intent5 = new Intent(this, FifthYear.class);
                startActivity(intent5);
                break;
            case R.id.syllabus6:
                Intent intent6 = new Intent(this, SixthYear.class);
                startActivity(intent6);
                break;
            default:
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
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
