package `in`.shaaan.pcipharmd

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.QuestionPapersUiState
import `in`.shaaan.pcipharmd.models.QuestionPapersViewModel
import `in`.shaaan.pcipharmd.models.YearInfo
import `in`.shaaan.pcipharmd.navigation.Screen

// WHAT: Added a private data class for ad placeholders within this file.
// WHY: To resolve the "Cannot access" error. Since this is a simple helper class used
//      only for structuring the list within this screen, defining it privately here is the
//      cleanest solution. It was previously only defined in HomeScreen.kt.

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.QuestionPapersScreen(
    navController: NavController,
    onAboutClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior,
    papersViewModel: QuestionPapersViewModel = viewModel()
) {
    val uiState by papersViewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is QuestionPapersUiState.Loading -> CircularProgressIndicator()
            is QuestionPapersUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is QuestionPapersUiState.Success -> {
                QuestionPapersContent(
                    yearList = state.yearList,
                    navController = navController,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.QuestionPapersContent(
    yearList: List<YearInfo>,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val listItems = remember(yearList) {
        val adPositions = setOf(2, 4, 6)
        val items = mutableListOf<Any>()
        yearList.forEachIndexed { index, yearInfo ->
            items.add(yearInfo)
            if (adPositions.contains(index + 1)) {
                items.add(AdPlaceholder(key = "ad_for_q_year_${yearInfo.year}"))
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
                        NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                    }
                }
            }
        }
    }
}
