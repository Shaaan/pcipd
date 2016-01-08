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

public class ThirdYear extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.tp2).setOnClickListener(this);
        findViewById(R.id.pharmac).setOnClickListener(this);
        findViewById(R.id.formulation).setOnClickListener(this);
        findViewById(R.id.analysis).setOnClickListener(this);
        findViewById(R.id.juris).setOnClickListener(this);
        findViewById(R.id.mchem).setOnClickListener(this);

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
            case R.id.tp2:
                String s = "https://shaaan.github.io/pcipd/syllabus3/tp2";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(s));
                break;
            case R.id.pharmac:
                String s1 = "https://shaaan.github.io/pcipd/syllabus3/pharmac";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder1.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder1.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(s1));
                break;
            case R.id.formulation:
                String s2 = "https://shaaan.github.io/pcipd/syllabus3/formulation";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder2.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder2.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(s2));
                break;
            case R.id.analysis:
                String s3 = "https://shaaan.github.io/pcipd/syllabus3/analysis";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder3.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder3.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s3));
                break;
            case R.id.juris:
                String s4 = "https://shaaan.github.io/pcipd/syllabus3/juris";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder4.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                builder4.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent4 = builder4.build();
                customTabsIntent4.launchUrl(this, Uri.parse(s4));
                break;
            case R.id.mchem:
                String s5 = "https://shaaan.github.io/pcipd/syllabus3/mchem";
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
