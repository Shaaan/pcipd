package `in`.shaaan.pcipharmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import `in`.shaaan.pcipharmd.ui.screen.AboutScreen
import `in`.shaaan.pcipharmd.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // WHAT: Initialize Firebase services and Remote Config.
        // WHY: To fetch the latest configuration values as early as possible in the app's lifecycle.
        // HOW: By calling the initialize() method on our RemoteConfigHelper.
        lifecycleScope.launch(Dispatchers.IO) {
            // Initialize Remote Config
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
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = Screen.Home.route,
        enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300)) + slideInHorizontally(
                initialOffsetX = { -it })
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutHorizontally(
                targetOffsetX = { it })
        }) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Year.route,
            arguments = Screen.Year.arguments
        ) { backStackEntry ->
            val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
            YearScreen(navController = navController, year = yearId)
        }
        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}
