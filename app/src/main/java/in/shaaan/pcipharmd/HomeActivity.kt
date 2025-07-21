package `in`.shaaan.pcipharmd

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
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
    // --- FIX 3: USE STANDARD MAIN DISPATCHER ---
    // What: Changed the CoroutineScope to use `Dispatchers.Main` instead of `Dispatchers.Main.immediate`.
    // Why: `Dispatchers.Main` is the standard, recommended dispatcher for UI-related tasks. While
    //      `Dispatchers.Main.immediate` can offer minor optimizations, it can also lead to unexpected
    //      execution order if not used carefully. For general-purpose UI scopes, the standard dispatcher
    //      is safer and more predictable.
    // How: Replaced `Dispatchers.Main.immediate` with `Dispatchers.Main`.
    private val activityScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@HomeActivity) { }
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        FirebaseAnalytics.getInstance(this)

        initializeUI()
        refreshAds()
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

    private fun refreshAds() {
        val contentHomeBinding = binding.layoutHome
        AdUtil.refreshAd(this, contentHomeBinding.nativeAd01, contentHomeBinding.nativeCard01)
        AdUtil.refreshAd(this, contentHomeBinding.nativeAd02, contentHomeBinding.nativeCard02)
        AdUtil.refreshAd(this, contentHomeBinding.nativeAd03, contentHomeBinding.nativeCard03)
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
        activityScope.cancel()
    }

    companion object {
        const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"
    }
}