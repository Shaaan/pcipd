package `in`.shaaan.pcipharmd.review

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.tasks.await

/**
 * WHAT: A helper class to manage the In-App Review flow.
 * WHY: To encapsulate the logic for requesting and showing the review dialog,
 * keeping the UI layer clean and the logic reusable.
 * HOW: It uses the Google Play Core library's ReviewManager to request a ReviewInfo
 * object and then launch the review flow.
 */
class InAppReviewHelper(private val context: Context) {

    private val manager = ReviewManagerFactory.create(context)

    suspend fun launchReviewFlow(activity: Activity) {
        try {
            val reviewInfo = manager.requestReviewFlow().await()
            manager.launchReviewFlow(activity, reviewInfo).await()
            Log.d("InAppReviewHelper", "Review flow launched successfully.")
        } catch (e: Exception) {
            // The API may throw an exception in various cases (e.g., no network).
            // It's important to catch it and log it, but not crash the app.
            Log.e("InAppReviewHelper", "Error launching review flow", e)
        }
    }
}
