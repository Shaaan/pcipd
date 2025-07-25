package `in`.shaaan.pcipharmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.config.RemoteConfigHelper
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.review.InAppReviewHelper
import `in`.shaaan.pcipharmd.ui.screen.AboutScreen
import `in`.shaaan.pcipharmd.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Create an instance of the InAppReviewHelper
    private val reviewHelper by lazy { InAppReviewHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch(Dispatchers.IO) {
            RemoteConfigHelper.initialize()
            val testDeviceIds = listOf("")
            val requestConfiguration = RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
            MobileAds.setRequestConfiguration(requestConfiguration)
            MobileAds.initialize(this@MainActivity) {}
        }
        FirebaseAnalytics.getInstance(this)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator(
                        onShowReview = {
                            lifecycleScope.launch {
                                reviewHelper.launchReviewFlow(this@MainActivity)
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(onShowReview: () -> Unit) {
    // Use the NavController from the Accompanist library
    val navController = rememberNavController()

    // SharedTransitionLayout is the root for all shared element transitions.
    SharedTransitionLayout {
        // AnimatedNavHost is the replacement for NavHost that enables transitions.
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    animatedVisibilityScope = this,
                    onShowReview = onShowReview
                )
            }
            composable(
                route = Screen.Year.route,
                arguments = Screen.Year.arguments
            ) { backStackEntry ->
                val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
                YearScreen(
                    navController = navController,
                    year = yearId,
                    animatedVisibilityScope = this
                )
            }
            composable(Screen.About.route) {
                AboutScreen(navController = navController)
            }
        }
    }
}
