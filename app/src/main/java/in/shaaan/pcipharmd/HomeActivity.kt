package `in`.shaaan.pcipharmd

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Main entry point activity for the application.
 * What: Displays the main home screen with navigation to different year syllabi and handles ad initialization.
 * Why: Serves as the primary navigation hub and initializes global components like AdMob.
 * How: Uses View Binding, handles click events, and manages a CoroutineScope for background tasks.
 */
class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // What: Initialize Google Mobile Ads SDK on a background thread.
        // Why: To prevent blocking the main UI thread during initialization, which is a best practice.
        // How: Uses a CoroutineScope with Dispatchers.IO.
        activityScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@HomeActivity) { }
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        FirebaseAnalytics.getInstance(this)

        initializeUI()
        refreshAd()
    }

    private fun initializeUI() {
        binding.layoutHome.apply {
            syllabus1.setOnClickListener(this@HomeActivity)
            syllabus2.setOnClickListener(this@HomeActivity)
            syllabus3.setOnClickListener(this@HomeActivity)
            syllabus4.setOnClickListener(this@HomeActivity)
            syllabus5.setOnClickListener(this@HomeActivity)
            syllabus6.setOnClickListener(this@HomeActivity)
        }

        binding.fab.setOnClickListener {
            Snackbar.make(binding.fab, getString(R.string.rate_app_message), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.rate_action)) {
                    val intent =
                        Intent(Intent.ACTION_VIEW, "market://details?id=$packageName".toUri())
                    startActivity(intent)
                }.show()
        }
    }

    private fun refreshAd() {
        val contentHomeBinding = binding.layoutHome
        AdUtil.loadNativeAd(this, contentHomeBinding.nativeAd01, contentHomeBinding.nativeCard01)
        AdUtil.loadNativeAd(this, contentHomeBinding.nativeAd02, contentHomeBinding.nativeCard02)
        AdUtil.refreshAd(
            this,
            contentHomeBinding.nativeAd03 as FrameLayout,
            contentHomeBinding.nativeCard03
        )
    }

    @SuppressLint("NonConstantResourceId")
    override fun onClick(view: View) {
        val activityClass: Class<*>? = when (view.id) {
            R.id.syllabus1 -> FirstYear::class.java
            R.id.syllabus2 -> SecondYear::class.java
            R.id.syllabus3 -> ThirdYear::class.java
            R.id.syllabus4 -> FourthYear::class.java
            R.id.syllabus5 -> FifthYear::class.java
            R.id.syllabus6 -> SixthYear::class.java
            else -> null
        }

        activityClass?.let {
            startActivity(Intent(this, it))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // What: Cancel all coroutines launched in this activity's scope.
        // Why: To prevent memory leaks and stop background work when the activity is destroyed.
        activityScope.cancel()
    }

    companion object {
        // What: A constant for the base URL.
        // Why: Removed the complex and unnecessary DataStore logic. This is simpler and more performant.
        // How: Defined as a public `const val` so it can be accessed from other classes like `BaseYearActivity`.
        const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"
    }
}
