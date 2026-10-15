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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.QuestionPapersUiState
import `in`.shaaan.pcipharmd.models.QuestionPapersViewModel
import `in`.shaaan.pcipharmd.models.YearInfo
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.ui.components.ShimmerGrid
import `in`.shaaan.pcipharmd.ui.components.YearCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.QuestionPapersScreen(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior,
    papersViewModel: QuestionPapersViewModel = viewModel()
) {
    val uiState by papersViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is QuestionPapersUiState.Loading -> ShimmerGrid(modifier = Modifier.fillMaxSize())
            is QuestionPapersUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is QuestionPapersUiState.Success -> {
                QuestionPapersContent(
                    yearList = state.yearList,
                    navController = navController,
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
private fun SharedTransitionScope.QuestionPapersContent(
    yearList: List<YearInfo>,
    navController: NavController,
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
                items.add(AdPlaceholder(key = "single_papers_ad"))
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
            key = { item -> if (item is YearInfo) "q_year_${item.year}" else (item as AdPlaceholder).key },
            span = { item -> if (item is AdPlaceholder) StaggeredGridItemSpan.FullLine else StaggeredGridItemSpan.SingleLane }
        ) { item ->
            when (item) {
                is YearInfo -> {
                    YearCard(
                        year = item.year,
                        subjectCount = item.subjectCount,
                        countLabel = "Papers",
                        onClick = {
                            navController.navigate(Screen.QuestionPaperList.createRoute(item.year))
                        },
                        modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "q-year-card-${item.year}"),
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
