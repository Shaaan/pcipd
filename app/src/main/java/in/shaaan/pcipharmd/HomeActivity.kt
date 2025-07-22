package `in`.shaaan.pcipharmd

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Main entry point activity for the application.
 * What: Displays the main home screen with navigation to different year syllabi and handles ad initialization.
 * Why: Serves as the primary navigation hub and initializes global components like AdMob.
 * How: Uses View Binding, handles click events, and manages a CoroutineScope for background tasks.
 */
class HomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding

    // What: A map to associate View objects directly with their target Activity classes.
    // Why: This completely eliminates the need for `findViewById` or `when` blocks for navigation,
    // making the code more robust, readable, and type-safe.
    private val navigationViewMap by lazy {
        with(binding.layoutHome) {
            mapOf(
                syllabus1 to 1,
                syllabus2 to 2,
                syllabus3 to 3,
                syllabus4 to 4,
                syllabus5 to 5,
                syllabus6 to 6
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
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
        // What: Set click listeners by iterating through the View objects in our navigation map.
        // Why: This is the cleanest approach, directly linking the view to its action without intermediate IDs.
        navigationViewMap.keys.forEach { view ->
            view.setOnClickListener(this)
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
        // What: Use a 'with' scope function for cleaner access to binding properties.
        // Why: Reduces redundant `binding.layoutHome` calls.
        with(binding.layoutHome) {
            val adPlaceholders = listOf(
                nativeAd01 to nativeCard01,
                nativeAd02 to nativeCard02,
                nativeAd03 to nativeCard03
            )

            adPlaceholders.forEach { (adFrame, cardView) ->
                // Use this@HomeActivity to clarify the context for AdUtil.
                AdUtil.refreshAd(this@HomeActivity, adFrame, cardView)
            }
        }
    }

    override fun onClick(view: View) {
        // What: Look up the target activity directly from the view object.
        // Why: This is the most direct and efficient way to handle navigation with this pattern.
        navigationViewMap[view]?.let { year ->
            val intent = Intent(this, YearActivity::class.java).apply {
                putExtra(SyllabusRepository.EXTRA_YEAR, year)
            }
            startActivity(intent)
        }
    }

    companion object {
        const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"
    }
}