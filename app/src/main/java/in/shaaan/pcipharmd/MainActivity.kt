package `in`.shaaan.pcipharmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.config.RemoteConfigHelper
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.review.InAppReviewHelper
import `in`.shaaan.pcipharmd.ui.screen.AboutScreen
import `in`.shaaan.pcipharmd.ui.screen.QuestionPaperListScreen
import `in`.shaaan.pcipharmd.ui.screen.QuestionPapersScreen
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
                    // WHAT: MainApp is the new root composable with bottom navigation.
                    // WHY: To provide the main navigation structure.
                    // HOW: It contains a Scaffold with a NavigationBar and the AppNavigator.
                    MainApp(
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

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainApp(onShowReview: () -> Unit) {
    val navController = rememberNavController()
    val bottomNavItems = listOf(Screen.Syllabus, Screen.QuestionPapers)

    SharedTransitionLayout {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                            label = { Text(screen.title!!) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            // AppNavigator now takes padding from the Scaffold.
            AppNavigator(
                modifier = Modifier.padding(innerPadding),
                onShowReview = onShowReview
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalAnimationApi::class)
@Composable
fun SharedTransitionScope.AppNavigator(onShowReview: () -> Unit, modifier: Modifier) {
    // Use the NavController from the Accompanist library
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Syllabus.route, // Start on the Syllabus tab
        modifier = modifier
    ) {
        // Main graph for the Syllabus section
        composable(Screen.Syllabus.route) {
            HomeScreen(
                onYearClick = { year -> navController.navigate(Screen.Year.createRoute(year)) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onShowReview = onShowReview,
                animatedVisibilityScope = this
            )
        }

        // Main graph for the Question Papers section
        composable(Screen.QuestionPapers.route) {
            QuestionPapersScreen(
                onYearClick = { year -> navController.navigate(Screen.QuestionPaperList.createRoute(year)) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                animatedVisibilityScope = this
            )
        }

        // Detail screen for Syllabus Year
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

        // Detail screen for Question Paper List
        composable(
            route = Screen.QuestionPaperList.route,
            arguments = Screen.QuestionPaperList.arguments
        ) { backStackEntry ->
            val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
            QuestionPaperListScreen(
                navController = navController,
                year = yearId
            )
        }

        // Shared About screen
        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}
