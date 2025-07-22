package `in`.shaaan.pcipharmd

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import `in`.shaaan.pcipharmd.databinding.ActivityYearBinding

/**
 * Abstract base activity for all year-specific syllabus activities.
 * What: Centralizes all common logic for displaying a list of subjects.
 * Why: To eliminate code duplication.
 * How: It now programmatically creates subject buttons and ad placeholders based on data provided by subclasses.
 */
class YearActivity : AppCompatActivity() {

    private lateinit var binding: ActivityYearBinding
    private val viewModel: BaseYearViewModel by viewModels()

    // --- FIX: ADD RECYCLERVIEW ADAPTER ---
    // What: A property to hold the instance of our new SyllabusAdapter.
    // Why: The adapter is the essential component that will manage the data and views for the RecyclerView.
    // How: Declared a lateinit var for the adapter.
    private lateinit var syllabusAdapter: SyllabusAdapter

    private lateinit var subjects: List<Pair<Int, String>>
    private lateinit var adPositions: List<Int>
    private val adCount: Int get() = adPositions.size


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This initial data-loading logic remains the same.
        val year = intent.getIntExtra(SyllabusRepository.EXTRA_YEAR, -1)
        val yearData = SyllabusRepository.getDataForYear(year)

        if (yearData == null) {
            Toast.makeText(this, "Error: Could not load data for the selected year.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        this.subjects = yearData.subjects
        this.adPositions = yearData.adPositions

        binding = ActivityYearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Year $year Syllabus"

        // --- FIX: REPLACE MANUAL VIEW CREATION WITH RECYCLERVIEW SETUP ---
        // What: Replaced the call to `populateSubjectsAndAds()` with calls to setup and populate the RecyclerView.
        // Why: This switches from an inefficient, manual process to the modern, performant Android standard for lists.
        // How: The old method call was removed and two new method calls were added.
        setupRecyclerView()
        populateList()

        initializeCommonUI()
    }

    // --- DELETED: ONDESTROY ---
    // What: The empty onDestroy() override was removed.
    // Why: An empty override serves no purpose and adds unnecessary boilerplate.
    // How: The method was deleted.

    private fun initializeCommonUI() {
        binding.fab.setOnClickListener { view ->
            showRateAppSnackbar(view)
        }
    }

    // --- FIX: ADD RECYCLERVIEW SETUP METHOD ---
    // What: A new method to initialize the RecyclerView and its adapter.
    // Why: Centralizes the setup logic for the RecyclerView, making `onCreate` cleaner.
    // How: It creates the adapter instance, passing a lambda function for click handling that
    //      delegates directly to the ViewModel. It then assigns the adapter and a LayoutManager
    //      to the RecyclerView found in the view binding.
    private fun setupRecyclerView() {
        syllabusAdapter = SyllabusAdapter { endpoint ->
            viewModel.onSubjectClicked(this, endpoint)
        }
        binding.contentLayout.subjectsRecyclerView.apply {
            adapter = syllabusAdapter
            layoutManager = LinearLayoutManager(this@YearActivity)
        }
    }

    // --- FIX: ADD LIST POPULATION METHOD ---
    // What: A new method to build the list of items for the adapter.
    // Why: This logic creates a single, unified list containing both subjects and ads in their
    //      correct positions, which the adapter can then easily display.
    // How: It iterates through the subjects, creating `ListItem.SubjectItem` and `ListItem.AdItem`
    //      objects and adding them to a list, which is then submitted to the adapter.
    private fun populateList() {
        val listItems = mutableListOf<ListItem>()
        var adCounter = 0

        subjects.forEachIndexed { index, subjectPair ->
            listItems.add(ListItem.SubjectItem(subjectPair.first, subjectPair.second))

            if (adPositions.contains(index + 1) && adCounter < adCount) {
                listItems.add(ListItem.AdItem(this))
                adCounter++
            }
        }
        syllabusAdapter.submitList(listItems)
    }


    // --- DELETED: OLD VIEW CREATION METHODS ---
    // What: Removed `populateSubjectsAndAds`, `createSubjectButton`, `createAdPlaceholderCard`,
    //       `loadAdForPlaceholder`, and the `onClick` method.
    // Why: All of these methods were part of the old, inefficient system of manually adding views
    //      to a LinearLayout. They are now completely replaced by the RecyclerView and its adapter.
    // How: The methods were deleted from the file.


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