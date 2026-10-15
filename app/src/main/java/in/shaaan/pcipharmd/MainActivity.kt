package `in`.shaaan.pcipharmd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.database
import `in`.shaaan.pcipharmd.config.RemoteConfigHelper
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.review.InAppReviewHelper
import `in`.shaaan.pcipharmd.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val reviewHelper by lazy { InAppReviewHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Firebase.database("https://pcipd-34920.firebaseio.com/").setPersistenceEnabled(true)
            } catch (e: Exception) {
                android.util.Log.d("MainActivity", "Firebase persistence configuration: ${e.message}")
            }
            RemoteConfigHelper.initialize()
            val requestConfiguration = RequestConfiguration.Builder()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                    }
                }
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
    val showQuestionPapers by RemoteConfigHelper.showQuestionPapersFlow.collectAsStateWithLifecycle()
    val bottomNavItems = if (showQuestionPapers) {
        listOf(Screen.Syllabus, Screen.QuestionPapers)
    } else {
        listOf(Screen.Syllabus)
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    SharedTransitionLayout {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                when (currentDestination?.route) {
                    Screen.Syllabus.route -> LargeTopAppBar(
                        title = { Text("Academic Syllabus") },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                                Icon(Icons.Default.Info, contentDescription = "About")
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                    Screen.QuestionPapers.route -> LargeTopAppBar(
                        title = { Text("Previous Papers") },
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
                    Screen.SubjectDetail.route -> {
                        TopAppBar(
                            title = { Text("Subject Details") },
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
                if (bottomNavItems.size > 1) {
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
                navController = navController,
                onShowReview = onShowReview,
                animatedVisibilityScope = this,
                scrollBehavior = scrollBehavior
            )
        }

        composable(Screen.QuestionPapers.route) {
            QuestionPapersScreen(
                navController = navController,
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
                animatedVisibilityScope = this,
                scrollBehavior = scrollBehavior
            )
        }

        composable(
            route = Screen.QuestionPaperList.route,
            arguments = Screen.QuestionPaperList.arguments
        ) { backStackEntry ->
            val yearId = backStackEntry.arguments?.getInt("yearId") ?: -1
            QuestionPaperListScreen(
                year = yearId,
                scrollBehavior = scrollBehavior
            )
        }

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
