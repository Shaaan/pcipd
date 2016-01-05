package in.shaaan.pcipharmd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import in.shaaan.pcipharmd.helper.CustomTabActivityHelper;
import in.shaaan.pcipharmd.helper.WebviewActivity;
import in.shaaan.pcipharmd.helper.WebviewFallback;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "View the curriculum of the PharmD course in India ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*public void syllabus1(View view) {
        String url = "https://about.me/shantanulondhe";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }*/

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

    /*@Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.syllabus1:
                String url = "https://google.com";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(url));
                break;
            case R.id.syllabus2:
                String url1 = "https://facebook.com";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder1.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(url1));
                break;
            case R.id.syllabus3:
                String syllabus3 = "http://gayatriagencies.co.in";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder3.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(syllabus3));
                break;
            case R.id.syllabus4:
                String syllabus4 = "http://prakelservices.com";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder4.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent4 = builder4.build();
                customTabsIntent4.launchUrl(this, Uri.parse(syllabus4));
                break;
            case R.id.syllabus5:
                String syllabus5 = "https://wikipedia.com";
                CustomTabsIntent.Builder builder5 = new CustomTabsIntent.Builder();
                builder5.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder5.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent5 = builder5.build();
                customTabsIntent5.launchUrl(this, Uri.parse(syllabus5));
                break;
            case R.id.syllabus6:
                String syllabus6 = "https://androidpolice.com";
                CustomTabsIntent.Builder builder6 = new CustomTabsIntent.Builder();
                builder6.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder6.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent6 = builder6.build();
                customTabsIntent6.launchUrl(this, Uri.parse(syllabus6));
                break;
            default:
        }
    }*/

    public void openUri(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra(WebviewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(intent);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://in.shaaan.pcipharmd/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Home Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://in.shaaan.pcipharmd/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
