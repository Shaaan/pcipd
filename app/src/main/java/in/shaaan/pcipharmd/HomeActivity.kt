package `in`.shaaan.pcipharmd

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.databinding.ActivityHomeBinding
import `in`.shaaan.pcipharmd.databinding.ContentHomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

// At the top level of your kotlin file:
val Context.dataStore by preferencesDataStore("settings")
val BASE_URL_KEY = stringPreferencesKey("base_url")

const val baseUrl = "https://pcipd.hypertex.co.in/syllabus/"


class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var yearBinding: ContentHomeBinding
    private lateinit var activityHomeBinding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityHomeBinding = ActivityHomeBinding.inflate(
            layoutInflater
        )
        setContentView(activityHomeBinding.root)
        setSupportActionBar(activityHomeBinding.toolbar)
        yearBinding = activityHomeBinding.layoutHome
        FirebaseAnalytics.getInstance(this)

        saveBaseUrl(this)

        Thread {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this) { }
        }
            .start()
        initializeUI()
        refreshAd()
    }

    private fun saveBaseUrl(context: Context) {
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[BASE_URL_KEY] = baseUrl
            }
        }
    }

    private fun initializeUI() {
        activityHomeBinding.layoutHome.syllabus1.setOnClickListener(this)
        activityHomeBinding.layoutHome.syllabus2.setOnClickListener(this)
        activityHomeBinding.layoutHome.syllabus3.setOnClickListener(this)
        activityHomeBinding.layoutHome.syllabus4.setOnClickListener(this)
        activityHomeBinding.layoutHome.syllabus5.setOnClickListener(this)
        activityHomeBinding.layoutHome.syllabus6.setOnClickListener(this)

        activityHomeBinding.fab.setOnClickListener { view: View? ->
            Snackbar.make(
                view!!, "Like the app? Rate it on Play Store!", Snackbar.LENGTH_LONG
            )
                .setAction("RATE") {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=in.shaaan.pcipharmd")
                    )
                    startActivity(intent)
                }.show()
        }
    }

    private fun refreshAd() {
        AdUtil.loadNativeAd(this, yearBinding.nativeAd01, yearBinding.nativeCard01)
        AdUtil.loadNativeAd(this, yearBinding.nativeAd02, yearBinding.nativeCard02)
        AdUtil.refreshAd(this, yearBinding.nativeAd03, yearBinding.nativeCard03)
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(view: View) {
        val id = view.id
        val activityClass: Class<*>? = when (id) {
            R.id.syllabus1 -> FirstYear::class.java
            R.id.syllabus2 -> SecondYear::class.java
            R.id.syllabus3 -> ThirdYear::class.java
            R.id.syllabus4 -> FourthYear::class.java
            R.id.syllabus5 -> FifthYear::class.java
            R.id.syllabus6 -> SixthYear::class.java
            else -> null
        }

        if (activityClass != null) {
            startActivity(Intent(this, activityClass))
        }
    }

    public override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        AdUtil.interstitialAd = null
    }

    companion object {
        fun getBaseUrl(context: Context): String {
            return runBlocking {
                val preferences = context.dataStore.data.first()
                preferences[BASE_URL_KEY].toString()
            }
        }
    }
}
