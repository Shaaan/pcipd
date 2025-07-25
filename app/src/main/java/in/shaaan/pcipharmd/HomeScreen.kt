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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.shaaan.pcipharmd.repository.SyllabusRepository

private data class AdPlaceholder(val key: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    onYearClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    onShowReview: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    // WHAT: Added scrollBehavior as a parameter.
    // WHY: To receive the scroll controller from the parent Scaffold in MainActivity.
    scrollBehavior: TopAppBarScrollBehavior
) {
    var engagementCount by rememberSaveable { mutableIntStateOf(0) }

    val listItems = remember {
        val years = (1..6).toList()
        val adPositions = setOf(2, 4, 6)
        val items = mutableListOf<Any>()
        years.forEachIndexed { index, year ->
            items.add(year)
            if (adPositions.contains(index + 1)) {
                items.add(AdPlaceholder(key = "ad_for_$year"))
            }
        }
        items
    }

    LaunchedEffect(Unit) {
        if (engagementCount > 0 && engagementCount % 3 == 0) {
            onShowReview()
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
        // WHAT: Applied the nestedScroll modifier.
        // WHY: This connects the grid's scrolling to the TopAppBar, allowing it to expand and collapse.
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        listItems.forEach { item ->
            when (item) {
                is Int -> { // This is a year
                    item(key = "s_year_$item") {
                        YearCard(
                            year = item,
                            subjectCount = SyllabusRepository.getDataForYear(item)?.subjects?.size ?: 0,
                            onClick = {
                                engagementCount++
                                onYearClick(item)
                            },
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "year-card-$item"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                                .animateItem()
                        )
                    }
                }
                is AdPlaceholder -> { // This is an ad
                    item(key = item.key, span = StaggeredGridItemSpan.FullLine) {
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
                text = "$subjectCount $countLabel",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
