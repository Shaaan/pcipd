package `in`.shaaan.pcipharmd

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import `in`.shaaan.pcipharmd.databinding.ActivityAboutBinding

/**
 * About Activity.
 * What: Displays information about the application.
 * Why: Provides a dedicated screen for app details.
 * How: Inflates a layout using View Binding and handles the Up button for navigation.
 */
class About : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // What: Inflate the layout using View Binding.
        // Why: Provides compile-time safety and avoids `findViewById`.
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // What: Set up the action bar to display an Up button.
        // Why: Provides consistent navigation back to the previous activity.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Handles action bar item clicks.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // What: Respond to the Up button press to navigate back.
        // Why: Ensures proper and standard navigation behavior.
        // How: Checks for the home item ID and uses the recommended `onBackPressedDispatcher`.
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
