package `in`.shaaan.pcipharmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.analytics.FirebaseAnalytics
import `in`.shaaan.pcipharmd.config.RemoteConfigHelper
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import `in`.shaaan.pcipharmd.review.InAppReviewHelper
import `in`.shaaan.pcipharmd.ui.screen.AboutScreen
import `in`.shaaan.pcipharmd.ui.screen.QuestionPaperListScreen
import `in`.shaaan.pcipharmd.ui.screen.QuestionPapersScreen
import `in`.shaaan.pcipharmd.ui.screen.SubjectDetailScreen
import `in`.shaaan.pcipharmd.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    SharedTransitionLayout {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                when (currentDestination?.route) {
                    Screen.Syllabus.route -> LargeTopAppBar(
                        title = { Text(stringResource(id = R.string.app_name)) },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                                Icon(Icons.Default.Info, contentDescription = "About")
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Screen.QuestionPapers.route -> LargeTopAppBar(
                        title = { Text("Previous Year Papers") },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                                Icon(Icons.Default.Info, contentDescription = "About")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                    Screen.Year.route -> {
                        val yearId = navBackStackEntry?.arguments?.getInt("yearId") ?: -1
                        LargeTopAppBar(
                            title = { Text("Year $yearId Syllabus") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                    Screen.QuestionPaperList.route -> {
                        val yearId = navBackStackEntry?.arguments?.getInt("yearId") ?: -1
                        LargeTopAppBar(
                            title = { Text("Year $yearId Papers") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                    // WHAT: Added a TopAppBar for the new SubjectDetail screen.
                    // WHY: To provide a title and back navigation for this screen.
                    Screen.SubjectDetail.route -> {
                        val yearId = navBackStackEntry?.arguments?.getInt("yearId") ?: -1
                        val subjectIndex = navBackStackEntry?.arguments?.getInt("subjectIndex") ?: -1
                        val subject = SyllabusRepository.getDataForYear(yearId)?.subjects?.getOrNull(subjectIndex)
                        TopAppBar(
                            title = { Text(subject?.let { stringResource(id = it.nameResId) } ?: "Subject Detail") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        )
                    }
                    Screen.About.route -> TopAppBar(
                        title = { Text("About") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            },
            bottomBar = {
                NavigationBar {
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
            AppNavigator(
                modifier = Modifier,
                navController = navController,
                onShowReview = onShowReview,
                paddingValues = innerPadding,
                scrollBehavior = scrollBehavior
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.AppNavigator(
    modifier: Modifier,
    navController: NavHostController,
    onShowReview: () -> Unit,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Syllabus.route,
        modifier = modifier.padding(paddingValues)
    ) {
        composable(Screen.Syllabus.route) {
            HomeScreen(
                onYearClick = { year -> navController.navigate(Screen.Year.createRoute(year)) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                onShowReview = onShowReview,
                animatedVisibilityScope = this,
                scrollBehavior = scrollBehavior
            )
        }

        composable(Screen.QuestionPapers.route) {
            QuestionPapersScreen(
                onYearClick = { year -> navController.navigate(Screen.QuestionPaperList.createRoute(year)) },
                onAboutClick = { navController.navigate(Screen.About.route) },
                animatedVisibilityScope = this,
                scrollBehavior = scrollBehavior
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

        composable(
            route = Screen.QuestionPaperList.route,
            arguments = Screen.QuestionPaperList.arguments
        ) { backStackEntry ->
            val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
            QuestionPaperListScreen(
                navController = navController,
                year = yearId,
                scrollBehavior = scrollBehavior
            )
        }

        // WHAT: Added the new composable to the navigation graph.
        // WHY: To make the SubjectDetailScreen accessible via its route.
        composable(
            route = Screen.SubjectDetail.route,
            arguments = Screen.SubjectDetail.arguments
        ) { backStackEntry ->
            val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
            val subjectIndex = backStackEntry.arguments?.getInt("subjectIndex") ?: -1
            SubjectDetailScreen(
                yearId = yearId,
                subjectIndex = subjectIndex
            )
        }

        composable(Screen.About.route) {
            AboutScreen(navController = navController)
        }
    }
}
