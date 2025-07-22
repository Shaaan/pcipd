package `in`.shaaan.pcipharmd

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel

// --- FIX 2: INTRODUCE VIEWMODEL ---
// What: A new ViewModel to manage UI-related data and logic for the BaseYearActivity.
// Why: ViewModels are lifecycle-aware, meaning they survive configuration changes (like screen rotation)
//      that destroy and recreate Activities. This makes them the ideal place to manage state and
//      long-running operations like loading ads, preventing redundant loads and simplifying the
//      Activity's responsibilities.
// How: Created a class extending AndroidViewModel to get access to the application context, which is
//      needed for initializing the ad.
class BaseYearViewModel(application: Application) : AndroidViewModel(application) {

    init {
        // What: Load the interstitial ad when the ViewModel is first created.
        // Why: The ViewModel's lifecycle is longer than the Activity's. This ensures the ad is loaded
        //      only once and persists across configuration changes, making the app more efficient.
        // How: Called the singleton AdUtil's load method with the application context.
        AdUtil.loadInterAd(application)
    }

    // What: A centralized function to handle the click event for a subject.
    // Why: Encapsulates the business logic (show ad, then navigate) in the ViewModel, keeping the
    //      Activity clean and focused only on UI events.
    // How: It constructs the final URL and calls the AdUtil to show the ad, which in turn handles
    //      the navigation to the URL.
    fun onSubjectClicked(activity: Activity, endpoint: String) {
        val url = "${SyllabusRepository.BASE_URL}$endpoint"
        AdUtil.showInterAd(activity, url)
    }
}