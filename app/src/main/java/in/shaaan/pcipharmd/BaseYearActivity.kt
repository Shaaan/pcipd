package `in`.shaaan.pcipharmd

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.databinding.ActivityYearBinding

/**
 * Abstract base activity for all year-specific syllabus activities.
 * What: Centralizes all common logic for displaying a list of subjects.
 * Why: To eliminate code duplication.
 * How: It now programmatically creates subject buttons and ad placeholders based on data provided by subclasses.
 */
abstract class BaseYearActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityYearBinding
    // --- FIX 1: REMOVE ADUTIL INSTANCE ---
    // What: Removed the local instance property `private val adUtil: AdUtil`.
    // Why: `AdUtil` is now a singleton `object`. Creating an instance is unnecessary and no longer possible.
    //      All calls should be made directly on the `AdUtil` object itself.
    // How: The property declaration was deleted.
    // private val adUtil: AdUtil = AdUtil()

    // --- Abstract members to be implemented by subclasses ---

    /**
     * Subclass must provide a list of Pairs. Each pair contains a StringRes for the
     * subject name and a String for the corresponding URL endpoint path.
     */
    abstract val subjects: List<Pair<Int, String>>

    /**
     * Subclass must provide the number of ads to be displayed.
     */
    abstract val adCount: Int

    /**
     * Defines at which positions to insert ads.
     * Example: listOf(2, 5) will insert an ad after the 2nd and 5th subject.
     */
    abstract val adPositions: List<Int>


    // --- Common Lifecycle Methods ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // --- FIX 1: UPDATE ADUTIL CALL ---
        // What: Changed the call to `loadInterAd` to use the singleton object.
        // Why: To align with the refactoring of `AdUtil` into a singleton.
        // How: The call was changed from `adUtil.loadInterAd(this)` to `AdUtil.loadInterAd(this)`.
        AdUtil.loadInterAd(this)

        populateSubjectsAndAds()
        initializeCommonUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        // --- FIX 1: REMOVE PROBLEMATIC STATE CLEARING ---
        // What: Removed the line `AdUtil.interstitialAd = null`.
        // Why: This line was causing a critical race condition. When navigating from one YearActivity to
        //      another, the `onDestroy` of the first activity would nullify the ad that the `onCreate`
        //      of the second activity had just started loading. The ad should only be nulled after it's
        //      shown or fails to show, which is handled within `AdUtil` itself.
        // How: The line was deleted.
        // AdUtil.interstitialAd = null
    }

    // --- UI and OnClick Logic ---

    private fun initializeCommonUI() {
        binding.fab.setOnClickListener { view ->
            showRateAppSnackbar(view)
        }
    }

    /**
     * What: Dynamically populates the LinearLayout with subject buttons and ad placeholders.
     * Why: To create the UI from a single generic layout, driven by data from subclasses.
     * How: Iterates through the `subjects` list, creates styled CardViews and Buttons for each,
     * and inserts ad placeholders at specified positions.
     */
    private fun populateSubjectsAndAds() {
        val container = binding.contentLayout.subjectsContainer
        var adCounter = 0

        subjects.forEachIndexed { index, subjectPair ->
            // Add subject button
            val button = createSubjectButton(subjectPair.first, subjectPair.second)
            container.addView(button)

            // Check if an ad should be inserted after this subject
            if (adPositions.contains(index + 1) && adCounter < adCount) {
                val adCard = createAdPlaceholderCard()
                container.addView(adCard)
                loadAdForPlaceholder(adCard)
                adCounter++
            }
        }
    }

    private fun createSubjectButton(@StringRes textResId: Int, endpoint: String): CardView {
        val cardView = LayoutInflater.from(this)
            .inflate(
                R.layout.template_subject_button,
                binding.contentLayout.subjectsContainer,
                false
            ) as CardView
        val button = cardView.findViewById<Button>(R.id.subject_button_template)
        button.setText(textResId)
        button.tag = endpoint // Store the endpoint in the button's tag
        button.setOnClickListener(this)
        return cardView
    }

    private fun createAdPlaceholderCard(): CardView {
        return LayoutInflater.from(this)
            .inflate(
                R.layout.template_ad_placeholder,
                binding.contentLayout.subjectsContainer,
                false
            ) as CardView
    }

    /**
     * What: Loads a custom unified ad into a given placeholder CardView.
     * Why: To use a single, consistent ad loading mechanism.
     * How: Calls the AdUtil.refreshAd method which handles the custom ad layout.
     */
    private fun loadAdForPlaceholder(adCard: CardView) {
        val adFrame = adCard.findViewById<FrameLayout>(R.id.ad_placeholder_template)
        AdUtil.refreshAd(this, adFrame, adCard)
    }


    override fun onClick(view: View) {
        // Retrieve the endpoint from the tag
        val endpoint = view.tag as? String
        endpoint?.let {
            val url = "${HomeActivity.BASE_URL}$it"
            // --- FIX 1: UPDATE ADUTIL CALL ---
            // What: Changed the call to `showInterAd` to use the singleton object.
            // Why: To align with the refactoring of `AdUtil` into a singleton.
            // How: The call was changed from `adUtil.showInterAd(this, url)` to `AdUtil.showInterAd(this, url)`.
            AdUtil.showInterAd(this, url)
        } ?: run {
            Log.w(
                "BaseYearActivity",
                "Clicked view does not have an endpoint tag."
            )
        }
    }

    // --- Common Menu Handling ---

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, About::class.java))
                true
            }

            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // --- Common Utility Methods ---

    private fun showRateAppSnackbar(view: View) {
        val appPackageName = packageName
        Snackbar.make(view, getString(R.string.rate_app_message), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.rate_action)) {
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            "market://details?id=$appPackageName".toUri()
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    try {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "http://play.google.com/store/apps/details?id=$appPackageName".toUri()
                            )
                        )
                    } catch (webE: ActivityNotFoundException) {
                        Toast.makeText(
                            this,
                            getString(R.string.play_store_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.show()
    }
}