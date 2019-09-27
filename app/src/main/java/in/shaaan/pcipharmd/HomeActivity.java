package in.shaaan.pcipharmd;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.nativeCard_03)
    CardView nativeCard13;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userPhoto;
    private Drawer result;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        findViewById(R.id.nativeCard_01).setVisibility(View.GONE);
        findViewById(R.id.nativeCard_02).setVisibility(View.GONE);
        nativeCard13.setVisibility(View.GONE);
        findViewById(R.id.syllabus1).setOnClickListener(this);
        findViewById(R.id.syllabus2).setOnClickListener(this);
        findViewById(R.id.syllabus3).setOnClickListener(this);
        findViewById(R.id.syllabus4).setOnClickListener(this);
        findViewById(R.id.syllabus5).setOnClickListener(this);
        findViewById(R.id.syllabus6).setOnClickListener(this);

//        Check if the user has signed up for chat
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

//        if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_syllabus);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_chat);

//      Create the AccountHeader
        String userName = "Anonymous";
        String userMail = "jane@doe.com";

        //initialize and create the image loader logic
        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder, String tag) {
                Glide.with(imageView.getContext())
                        .load(uri)
                        .apply(new RequestOptions()
                                .placeholder(placeholder))
                        .into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.with(imageView.getContext()).clear(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });


        userPhoto = "https://google.com";
//      If the user has logged in to chat, set his custom image and name
        if (mUser != null) {
            userName = mUser.getDisplayName();
            userMail = mUser.getEmail();
            if (mUser.getPhotoUrl() != null) {
                userPhoto = mUser.getPhotoUrl().toString();
            }

        }


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.md_white_1000)
                .addProfiles(
                        new ProfileDrawerItem().withName(userName).withEmail(userMail).withIcon(userPhoto)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .withDividerBelowHeader(true)
                .withSavedInstance(savedInstanceState)
                .build();

        //create the drawer and remember the `Drawer` result object
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(-1)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.action_about)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        Log.d("Drawer", "Clicked" + position);
                        switch (position) {
                            case 1:
                                Toast toast = Toast.makeText(HomeActivity.this, "Already viewing the syllabus..", Toast.LENGTH_SHORT);
                                toast.show();
                                break;

                            case 2:
                                Intent chatIntent = new Intent(HomeActivity.this, ChatActivity.class);
                                startActivity(chatIntent);
                                result.closeDrawer();
                                break;

                            case 3:
                                startActivity(new Intent(HomeActivity.this, About.class));
                                result.closeDrawer();
                                break;
                        }
                        return true;
                    }
                })
                .addStickyDrawerItems()
                .withFullscreen(true)
                .withSavedInstance(savedInstanceState)
                .build();


        FloatingActionButton fab = findViewById(R.id.fab);
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

//
//
//        Google AdMob

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        refreshAd();
    }

    public void refreshAd() {
//
//        Banner Ads
//
//        AdView mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("A86F9B85802FF794F2D5CE913677792C").build();
//        mAdView.loadAd(adRequest);

//
//        Native Ads
//
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        findViewById(R.id.nativeCard_02).setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        TemplateView templateView = findViewById(R.id.native_ad_02);
                        templateView.setStyles(style);
                        templateView.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        AdLoader adLoader1 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        findViewById(R.id.nativeCard_01).setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        TemplateView templateView = findViewById(R.id.native_ad_01);
                        templateView.setStyles(style);
                        templateView.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());
        adLoader1.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());

        AdLoader adLoader2 = new AdLoader.Builder(this, "ca-app-pub-1941738066609841/8926036161")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        nativeCard13.setVisibility(View.VISIBLE);
                        NativeTemplateStyle style = new NativeTemplateStyle.Builder()
                                .withSecondaryTextSize(0)
                                .withTertiaryTextSize(0)
                                .withCallToActionTextSize(0)
                                .build();

                        TemplateView templateView = findViewById(R.id.native_ad_03);
                        templateView.setStyles(style);
                        templateView.setNativeAd(unifiedNativeAd);
                    }
                }).build();

        adLoader2.loadAd(new AdRequest.Builder().addTestDevice("5FFDEB2790F9F640D76A0B9FC0D2BCD9").addTestDevice("010E297A73E360936A053C01A2D8902F").build());
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
//        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, About.class));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
