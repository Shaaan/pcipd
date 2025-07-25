package `in`.shaaan.pcipharmd.config

import android.util.Log
import `in`.shaaan.pcipharmd.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

/**
 * WHAT: A singleton object to manage Firebase Remote Config.
 * WHY: To centralize the logic for fetching and providing remotely configured values,
 * like AdMob IDs. This makes the app's configuration dynamic and easier to update
 * without releasing a new version.
 * HOW: It initializes Remote Config, sets default values from BuildConfig, and provides
 * a function to fetch the latest values from the Firebase backend.
 */
object RemoteConfigHelper {

    private const val TAG = "RemoteConfigHelper"

    // Define keys for the Remote Config parameters
    private const val INTERSTITIAL_AD_UNIT_ID_KEY = "interstitial_ad_unit_id"
    private const val NATIVE_AD_UNIT_ID_KEY = "native_ad_unit_id"

    // Get the Firebase Remote Config instance
    private val remoteConfig = Firebase.remoteConfig

    fun initialize() {
        // Configure settings for Remote Config
        val configSettings = remoteConfigSettings {
            // Set a minimum fetch interval to avoid throttling during development.
            // Use a higher value (e.g., 12 hours) in production.
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // WHAT: Explicitly define the type of the map as Map<String, Any>.
        // WHY: To resolve the type inference error. The setDefaultsAsync method requires a
        //      Map<String, Any>, and explicitly setting the type ensures the compiler
        //      doesn't infer a more specific but incompatible type like Map<String, String>.
        // HOW: Added ': Map<String, Any>' to the variable declaration.
        val defaults: Map<String, Any> = mapOf(
            INTERSTITIAL_AD_UNIT_ID_KEY to BuildConfig.INTERSTITIAL_AD_UNIT_ID,
            NATIVE_AD_UNIT_ID_KEY to BuildConfig.NATIVE_AD_UNIT_ID
        )
        remoteConfig.setDefaultsAsync(defaults)

        // Fetch and activate the latest remote values
        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                } else {
                    Log.w(TAG, "Config fetch failed")
                }
            }
    }

    // Getter for the Interstitial Ad Unit ID
    val interstitialAdUnitId: String
        get() = remoteConfig.getString(INTERSTITIAL_AD_UNIT_ID_KEY)

    // Getter for the Native Ad Unit ID
    val nativeAdUnitId: String
        get() = remoteConfig.getString(NATIVE_AD_UNIT_ID_KEY)
}
