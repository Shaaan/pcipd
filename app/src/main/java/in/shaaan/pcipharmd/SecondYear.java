package in.shaaan.pcipharmd;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SecondYear extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.cology).setOnClickListener(this);
        findViewById(R.id.tp1).setOnClickListener(this);
        findViewById(R.id.cognosy).setOnClickListener(this);
        findViewById(R.id.micro).setOnClickListener(this);
        findViewById(R.id.patho).setOnClickListener(this);
        findViewById(R.id.com).setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.cology:
                String s3 = "https://shaaan.github.io/pcipd/syllabus2/cology";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder3.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent3 = builder3.build();
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                customTabsIntent3.launchUrl(this, Uri.parse(s3));
                break;
            case R.id.tp1:
                String s = "https://shaaan.github.io/pcipd/syllabus2/tp1";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(s));
                break;
            case R.id.micro:
                String s1 = "https://shaaan.github.io/pcipd/syllabus2/micro";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder1.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(s1));
                break;
            case R.id.cognosy:
                String s2 = "https://shaaan.github.io/pcipd/syllabus2/cognosy";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder2.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(s2));
                break;
            case R.id.patho:
                String s4 = "https://shaaan.github.io/pcipd/syllabus2/patho";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder4.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder4.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent4 = builder4.build();
                customTabsIntent4.launchUrl(this, Uri.parse(s4));
                break;
            case R.id.com:
                String s5 = "https://shaaan.github.io/pcipd/syllabus2/cmp";
                CustomTabsIntent.Builder builder5 = new CustomTabsIntent.Builder();
                builder5.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder5.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder5.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent5 = builder5.build();
                customTabsIntent5.launchUrl(this, Uri.parse(s5));
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
