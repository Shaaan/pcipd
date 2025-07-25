package `in`.shaaan.pcipharmd

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Looks3
import androidx.compose.material.icons.filled.Looks4
import androidx.compose.material.icons.filled.Looks5
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.LooksTwo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onShowReview: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val years = remember { (1..6).toList() }
    val adPositions = remember { setOf(2, 4, 6) }

    // WHAT: A counter to track user engagement.
    // WHY: To trigger the in-app review prompt after the user has demonstrated
    //      sufficient engagement (viewing 3 syllabuses), which increases the
    //      likelihood of receiving a thoughtful rating.
    // HOW: Using rememberSaveable to persist the count across configuration changes.
    var engagementCount by rememberSaveable { mutableIntStateOf(0) }

    // This effect runs when the HomeScreen becomes visible again.
    LaunchedEffect(Unit) {
        if (engagementCount > 0 && engagementCount % 3 == 0) {
            onShowReview()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalItemSpacing = 16.dp,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            years.forEachIndexed { index, year ->
                item(key = "year_$year") {
                    YearCard(
                        year = year,
                        subjectCount = SyllabusRepository.getDataForYear(year)?.subjects?.size ?: 0,
                        onClick = {
                            engagementCount++ // Increment engagement on click
                            navController.navigate(Screen.Year.createRoute(year))
                        },
                        modifier = Modifier
                            // WHAT: Applied the sharedElement modifier.
                            // WHY: To link this card with the destination screen's app bar for a smooth transition.
                            // HOW: Using a unique key for each year.
                            .sharedElement(
                                rememberSharedContentState(key = "year-card-$year"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .animateItem()
                    )
                }
                if (adPositions.contains(year)) {
                    item(key = "ad_for_$year", span = StaggeredGridItemSpan.FullLine) {
                        Card(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .animateItem(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun YearCard(year: Int, onClick: () -> Unit, modifier: Modifier = Modifier, subjectCount: Int) {
    val icon = when (year) {
        1 -> Icons.Default.LooksOne
        2 -> Icons.Default.LooksTwo
        3 -> Icons.Default.Looks3
        4 -> Icons.Default.Looks4
        5 -> Icons.Default.Looks5
        6 -> Icons.Default.Looks6
        else -> Icons.AutoMirrored.Filled.List
    }
    val text = when (year) {
        1 -> "First Year"
        2 -> "Second Year"
        3 -> "Third Year"
        4 -> "Fourth Year"
        5 -> "Fifth Year"
        6 -> "Sixth Year"
        else -> ""
    }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Year $year",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$subjectCount Subjects",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
