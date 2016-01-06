package in.shaaan.pcipharmd;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FifthYear extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.epi).setOnClickListener(this);
        findViewById(R.id.clerkship).setOnClickListener(this);
        findViewById(R.id.tdm).setOnClickListener(this);
        findViewById(R.id.research).setOnClickListener(this);
        findViewById(R.id.tp3).setOnClickListener(this);

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
            case R.id.epi:
                String epi = "https://shaaan.github.io/pcipd/syllabus5/epi/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(epi));
                break;
            case R.id.research:
                String s = "https://shaaan.github.io/pcipd/syllabus5/research";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder1.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(s));
                break;
            case R.id.tdm:
                String s1 = "https://shaaan.github.io/pcipd/syllabus5/tdm";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder2.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(s1));
                break;
            //// TODO: 6/1/16 Shift this to Fourth Year 
            
            /*case R.id.tp3:
                String s3 = "https://shaaan.github.io/pcipd/syllabus5/epi";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder3.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s3));
                break;*/
            default:
        }
    }
}
