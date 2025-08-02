package `in`.shaaan.pcipharmd

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.HomeScreenUiState
import `in`.shaaan.pcipharmd.models.HomeViewModel
import `in`.shaaan.pcipharmd.models.YearInfo
import `in`.shaaan.pcipharmd.navigation.Screen

data class AdPlaceholder(val key: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController, // WHAT: Changed parameter from a lambda to the NavController.
    onShowReview: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior,
    homeViewModel: HomeViewModel = viewModel()
) {
    var engagementCount by rememberSaveable { mutableIntStateOf(0) }
    val uiState by homeViewModel.uiState.collectAsState()

    LaunchedEffect(engagementCount) {
        if (engagementCount > 0 && engagementCount % 3 == 0) {
            onShowReview()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is HomeScreenUiState.Loading -> CircularProgressIndicator()
            is HomeScreenUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is HomeScreenUiState.Success -> {
                HomeScreenContent(
                    yearList = state.yearList,
                    // WHAT: Pass the NavController and engagement counter logic down.
                    // WHY: To connect the user's click action to the navigation and review logic.
                    onYearClick = { year ->
                        engagementCount++
                        navController.navigate(Screen.Year.createRoute(year))
                    },
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.HomeScreenContent(
    yearList: List<YearInfo>,
    onYearClick: (Int) -> Unit, // The lambda is now handled correctly inside the parent.
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val listItems = remember(yearList) {
        val adPositions = setOf(2, 4, 6)
        val items = mutableListOf<Any>()
        yearList.forEachIndexed { index, yearInfo ->
            items.add(yearInfo)
            if (adPositions.contains(index + 1)) {
                items.add(AdPlaceholder(key = "ad_for_${yearInfo.year}"))
            }
        }
        items
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = listItems,
            key = { item -> if (item is YearInfo) "year_${item.year}" else (item as AdPlaceholder).key },
            span = { item -> if (item is AdPlaceholder) StaggeredGridItemSpan.FullLine else StaggeredGridItemSpan.SingleLane }
        ) { item ->
            when (item) {
                is YearInfo -> {
                    YearCard(
                        year = item.year,
                        subjectCount = item.subjectCount,
                        // WHAT: The onClick action is now handled by the parent composable.
                        // WHY: This keeps the YearCard reusable and delegates the specific click
                        //      logic (navigation, counting) to the screen-level composable.
                        onClick = { onYearClick(item.year) },
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "year-card-${item.year}"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                            .animateItem()
                    )
                }
                is AdPlaceholder -> {
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

@Composable
fun YearCard(year: Int, onClick: () -> Unit, modifier: Modifier = Modifier, subjectCount: Int, countLabel: String = "Subjects") {
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                text = "$subjectCount $countLabel",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
