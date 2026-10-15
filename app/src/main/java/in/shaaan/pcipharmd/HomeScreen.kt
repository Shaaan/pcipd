package `in`.shaaan.pcipharmd

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.HomeScreenUiState
import `in`.shaaan.pcipharmd.models.HomeViewModel
import `in`.shaaan.pcipharmd.models.YearInfo
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.ui.components.ShimmerGrid
import `in`.shaaan.pcipharmd.ui.components.YearCard

data class AdPlaceholder(val key: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    navController: NavController,
    onShowReview: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior,
    homeViewModel: HomeViewModel = viewModel()
) {
    var engagementCount by rememberSaveable { mutableIntStateOf(0) }
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

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
            is HomeScreenUiState.Loading -> ShimmerGrid(modifier = Modifier.fillMaxSize())
            is HomeScreenUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is HomeScreenUiState.Success -> {
                HomeScreenContent(
                    yearList = state.yearList,
                    onYearClick = { year ->
                        engagementCount++
                        navController.navigate(Screen.Year.createRoute(year))
                    },
                    animatedVisibilityScope = animatedVisibilityScope,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.HomeScreenContent(
    yearList: List<YearInfo>,
    onYearClick: (Int) -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    var isAdFailed by remember { mutableStateOf(false) }

    val listItems = remember(yearList, isAdFailed) {
        val items = mutableListOf<Any>()
        yearList.forEachIndexed { index, yearInfo ->
            items.add(yearInfo)
            if (index == 1 && !isAdFailed) {
                items.add(AdPlaceholder(key = "single_home_ad"))
            }
        }
        items
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                        NativeAdComposable(
                            adUnitId = AdUtil.NATIVE_AD_UNIT_ID,
                            onAdFailed = { isAdFailed = true }
                        )
                    }
                }
            }
        }
    }
}
