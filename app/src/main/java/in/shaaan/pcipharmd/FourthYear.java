package in.shaaan.pcipharmd;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FourthYear extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.tp3).setOnClickListener(this);
        findViewById(R.id.toxicology).setOnClickListener(this);
        findViewById(R.id.hp).setOnClickListener(this);
        findViewById(R.id.cp).setOnClickListener(this);
        findViewById(R.id.biopharm).setOnClickListener(this);
        findViewById(R.id.biostat).setOnClickListener(this);

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
            case R.id.tp3:
                String s3 = "https://shaaan.github.io/pcipd/syllabus4/pt3";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder3.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s3));
                break;
            case R.id.toxicology:
                String s = "https://shaaan.github.io/pcipd/syllabus4/toxicology";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(s));
                break;
            case R.id.cp:
                String s1 = "https://shaaan.github.io/pcipd/syllabus4/cp";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder1.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(s1));
                break;
            case R.id.hp:
                String s2 = "https://shaaan.github.io/pcipd/syllabus4/hosp_pharm";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder2.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(s2));
                break;
            case R.id.biopharm:
                String s4 = "https://shaaan.github.io/pcipd/syllabus4/biopharm";
                CustomTabsIntent.Builder builder4 = new CustomTabsIntent.Builder();
                builder4.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder4.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent4 = builder4.build();
                customTabsIntent4.launchUrl(this, Uri.parse(s4));
                break;
            case R.id.biostat:
                String s5 = "https://shaaan.github.io/pcipd/syllabus4/biostat";
                CustomTabsIntent.Builder builder5 = new CustomTabsIntent.Builder();
                builder5.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder5.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent5 = builder5.build();
                customTabsIntent5.launchUrl(this, Uri.parse(s5));
                break;
        }
    }
}
