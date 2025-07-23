package `in`.shaaan.pcipharmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.ui.screen.AboutScreen
import `in`.shaaan.pcipharmd.ui.screen.YearScreen
import `in`.shaaan.pcipharmd.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // What: Initialize AdMob and Analytics.
        // Why: These services need to be initialized once when the app starts.
        // How: Launched in a coroutine to avoid blocking the main thread.
        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(this@MainActivity) {}
        }
        FirebaseAnalytics.getInstance(this)

        // What: Set the activity's content to a Composable UI.
        // Why: This is the entry point for a Jetpack Compose application.
        // How: The `setContent` block defines the entire UI hierarchy.
        setContent {
            // What: Use a custom AppTheme.
            // Why: Centralizes theme attributes like colors and typography.
            // How: Wraps the entire app content in the AppTheme composable.
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
//            AppNavigator()
        }
    }
}

@Composable
fun AppNavigator() {
    // What: A NavController to manage app navigation.
    // Why: It keeps track of the back stack and allows navigation between screens.
    // How: Created using `rememberNavController()`.
    val navController = rememberNavController()

    // What: The NavHost defines the navigation graph.
    // Why: It maps string "routes" to the composable functions that should be displayed.
    // How: It links routes like "home" and "year/{yearId}" to HomeScreen and YearScreen composables.
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "year/{yearId}",
            arguments = listOf(navArgument("yearId") { type = NavType.IntType })
        ) { backStackEntry ->
            val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
            YearScreen(navController = navController, year = yearId)
        }
        composable("about") {
            AboutScreen(navController = navController)
        }
    }
}