package `in`.shaaan.pcipharmd

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.viewbinding.ViewBinding
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.net.toUri

// What: A typealias for the binding inflater function.
// Why: To simplify the constructor signature of the base class, making it cleaner.
// How: Defines a shorter, more descriptive name for the function type.
typealias ActivityBindingInflater<VB> = (inflater: LayoutInflater) -> VB

/**
 * Abstract base activity for all year-specific syllabus activities.
 * What: Centralizes all common logic.
 * Why: To eliminate code duplication in subclasses (`FirstYear`, `SecondYear`, etc.), making the codebase easier to maintain.
 * How: It handles toolbar setup, FAB actions, ad loading, menu creation, and click handling. Subclasses only need to provide their specific layouts and data.
 *
 * @param VB The type of the Activity's ViewBinding.
 * @param CB The type of the Content's ViewBinding (for the included layout).
 * @param activityBindingInflater A function to inflate the Activity's ViewBinding.
 */
abstract class BaseYearActivity<VB : ViewBinding, CB : ViewBinding>(
    private val activityBindingInflater: ActivityBindingInflater<VB>
) : AppCompatActivity(), View.OnClickListener {

    // Protected properties accessible to subclasses
    protected lateinit var activityBinding: VB
    protected lateinit var contentBinding: CB
    // What: An instance of AdUtil.
    // Why: Each activity can manage its own ad requests if needed, though the companion object in AdUtil shares the ad instances.
    protected val adUtil: AdUtil = AdUtil()

    // --- Abstract members to be implemented by subclasses ---

    /**
     * Subclass must provide its specific Content Binding instance.
     */
    abstract fun getContentBinding(activityBinding: VB): CB

    /**
     * Subclass must provide the Toolbar view from its Activity Binding.
     */
    abstract fun getToolbar(activityBinding: VB): Toolbar

    /**
     * Subclass must provide the FloatingActionButton from its Activity Binding.
     */
    abstract fun getFab(activityBinding: VB): FloatingActionButton

    /**
     * Subclass must provide a map where the key is the R.id of a subject button
     * and the value is the corresponding URL endpoint path.
     */
    @get:IdRes
    abstract val subjectButtonIdToEndpointMap: Map<Int, String>

    /**
     * Subclass must provide a list of Pairs, containing the Native Ad View and its CardView container.
     */
    abstract fun getNativeAdPlaceholderPairs(contentBinding: CB): List<Pair<View, View>>

    /**
     * Subclass must provide a list of all subject button Views.
     */
    abstract fun getSubjectButtons(contentBinding: CB): List<View>

    // --- Common Lifecycle Methods ---

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // What: Inflate the Activity Binding using the function provided by the subclass.
        // Why: This is the core of the abstraction, allowing the base class to work with any layout.
        // How: The subclass passes its `Activity...Binding::inflate` method reference to the constructor.
        activityBinding = activityBindingInflater.invoke(layoutInflater)
        setContentView(activityBinding.root)

        contentBinding = getContentBinding(activityBinding)

        setSupportActionBar(getToolbar(activityBinding))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adUtil.loadInterAd(this)

        initializeCommonUI()
        refreshAds()
    }

    override fun onDestroy() {
        super.onDestroy()
        // What: Clear the static interstitial ad reference.
        // Why: To prevent memory leaks when the activity is destroyed.
        // How: Sets the static property in AdUtil to null.
        AdUtil.interstitialAd = null
    }

    // --- Common UI Initialization ---

    private fun initializeCommonUI() {
        getFab(activityBinding).setOnClickListener { view ->
            showRateAppSnackbar(view)
        }

        // What: Set the common onClick listener for all subject buttons.
        // Why: Avoids setting individual listeners in each subclass.
        // How: Iterates through the list of buttons provided by the subclass.
        getSubjectButtons(contentBinding).forEach { button ->
            button.setOnClickListener(this)
        }
    }

    // --- Common Ad Refresh Logic ---

    private fun refreshAds() {
        val adPlaceholders = getNativeAdPlaceholderPairs(contentBinding)

        // What: Iterates through ad placeholders and loads ads.
        // Why: Centralizes ad loading logic. It's now flexible for different numbers and types of ads.
        // How: Uses a `forEach` loop and `when` to handle different ad view types.
        adPlaceholders.forEachIndexed { index, (adView, cardView) ->
            when (adView) {
                is TemplateView -> AdUtil.loadNativeAd(this, adView, cardView)
                is FrameLayout -> {
                    // This assumes that FrameLayouts are used for the custom native ad.
                    AdUtil.refreshAd(this, adView, cardView as CardView)
                }
                else -> Log.e("BaseYearActivity", "Unsupported ad view type: ${adView::class.simpleName}")
            }
        }
    }

    // --- Common OnClick Logic ---

    override fun onClick(view: View) {
        // What: Retrieves the URL endpoint from the map provided by the subclass.
        // Why: Centralizes navigation logic. The base class doesn't need to know about specific subjects.
        // How: Uses the `subjectButtonIdToEndpointMap` to find the endpoint for the clicked button's ID.
        val endpoint = subjectButtonIdToEndpointMap[view.id]

        endpoint?.let {
            // What: Construct the full URL using the constant from HomeActivity.
            // Why: Removes the need for DataStore and `runBlocking` to get the base URL. It's a constant.
            val url = "${HomeActivity.BASE_URL}$it"
            adUtil.showInterAd(this, url)
        } ?: run {
            Log.w("BaseYearActivity", "Clicked view with ID ${resources.getResourceEntryName(view.id)} not found in endpoint map.")
        }
    }

    // --- Common Menu Handling ---

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // What: Inflates a common menu resource.
        // Why: Ensures all year activities have the same menu options (e.g., "About").
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // What: Handles common menu actions.
        // Why: Avoids duplicating this logic in every subclass.
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, About::class.java))
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed() // Handles the "Up" button.
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
                    startActivity(Intent(Intent.ACTION_VIEW,
                        "market://details?id=$appPackageName".toUri()))
                } catch (e: ActivityNotFoundException) {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW,
                            "http://play.google.com/store/apps/details?id=$appPackageName".toUri()))
                    } catch (webE: ActivityNotFoundException) {
                        Toast.makeText(this, getString(R.string.play_store_error), Toast.LENGTH_SHORT).show()
                    }
                }
            }.show()
    }
}
