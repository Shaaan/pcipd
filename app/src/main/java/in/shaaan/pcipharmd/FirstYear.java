package in.shaaan.pcipharmd;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FirstYear extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_year);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.hap).setOnClickListener(this);
        findViewById(R.id.ceutics).setOnClickListener(this);
        findViewById(R.id.biochem).setOnClickListener(this);
        findViewById(R.id.oc).setOnClickListener(this);
        findViewById(R.id.ic).setOnClickListener(this);


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
            case R.id.hap:
                String hap = "https://shaaan.github.io/pcipd/syllabus1/hap/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(hap));
                break;
            case R.id.ceutics:
                String ceutics = "https://shaaan.github.io/pcipd/syllabus1/pahrmaceutics/";
                CustomTabsIntent.Builder builderC = new CustomTabsIntent.Builder();
                builderC.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builderC.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntentC = builderC.build();
                customTabsIntentC.launchUrl(this, Uri.parse(ceutics));
                break;
            case R.id.biochem:
                String biochem = "https://shaaan.github.io/pcipd/syllabus1/biochem/";
                CustomTabsIntent.Builder builder1 = new CustomTabsIntent.Builder();
                builder1.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder1.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent1 = builder1.build();
                customTabsIntent1.launchUrl(this, Uri.parse(biochem));
                break;
            case R.id.ic:
                String ic = "https://shaaan.github.io/pcipd/syllabus1/ic/";
                CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
                builder2.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder2.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent2 = builder2.build();
                customTabsIntent2.launchUrl(this, Uri.parse(ic));
                break;
            case R.id.oc:
                String s = "https://shaaan.github.io/pcipd/syllabus1/oc/";
                CustomTabsIntent.Builder builder3 = new CustomTabsIntent.Builder();
                builder3.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
                builder3.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
                CustomTabsIntent customTabsIntent3 = builder3.build();
                customTabsIntent3.launchUrl(this, Uri.parse(s));
                break;
            default:
        }
    }


}