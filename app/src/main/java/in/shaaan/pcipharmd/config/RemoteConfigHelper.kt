package `in`.shaaan.pcipharmd.config

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import `in`.shaaan.pcipharmd.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * WHAT: A singleton object to manage Firebase Remote Config.
 * WHY: To centralize the logic for fetching and providing remotely configured values,
 * like AdMob IDs and feature flags (e.g. showing Question Papers).
 * HOW: It initializes Remote Config, sets default values from BuildConfig, and provides
 * a StateFlow to observe feature flag updates in real-time.
 */
object RemoteConfigHelper {

    private const val TAG = "RemoteConfigHelper"

    // Define keys for the Remote Config parameters
    private const val INTERSTITIAL_AD_UNIT_ID_KEY = "interstitial_ad_unit_id"
    private const val NATIVE_AD_UNIT_ID_KEY = "native_ad_unit_id"
    private const val SHOW_QUESTION_PAPERS_KEY = "show_question_papers"

    // Get the Firebase Remote Config instance
    private val remoteConfig = Firebase.remoteConfig

    private val _showQuestionPapersFlow = MutableStateFlow(false)
    val showQuestionPapersFlow: StateFlow<Boolean> = _showQuestionPapersFlow.asStateFlow()

    fun initialize() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 60 else 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val defaults: Map<String, Any> = mapOf(
            INTERSTITIAL_AD_UNIT_ID_KEY to BuildConfig.INTERSTITIAL_AD_UNIT_ID,
            NATIVE_AD_UNIT_ID_KEY to BuildConfig.NATIVE_AD_UNIT_ID,
            SHOW_QUESTION_PAPERS_KEY to false
        )
        remoteConfig.setDefaultsAsync(defaults)

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
                _showQuestionPapersFlow.value = showQuestionPapers
            }
    }

    // Getter for the Interstitial Ad Unit ID
    val interstitialAdUnitId: String
        get() = remoteConfig.getString(INTERSTITIAL_AD_UNIT_ID_KEY)

    // Getter for the Native Ad Unit ID
    val nativeAdUnitId: String
        get() = remoteConfig.getString(NATIVE_AD_UNIT_ID_KEY)

    // Getter for Question Papers visibility feature flag
    val showQuestionPapers: Boolean
        get() = remoteConfig.getBoolean(SHOW_QUESTION_PAPERS_KEY)
}
