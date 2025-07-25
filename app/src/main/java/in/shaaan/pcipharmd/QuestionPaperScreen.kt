package `in`.shaaan.pcipharmd.ui.screen

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.NativeAdComposable
import `in`.shaaan.pcipharmd.YearCard
import `in`.shaaan.pcipharmd.repository.QuestionPaperRepository

// WHAT: Added a private data class for ad placeholders.
// WHY: To create a typed item for inserting into the list for the staggered grid.
private data class AdPlaceholder(val key: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.QuestionPapersScreen(
    onYearClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior
) {
    // WHAT: Created a list of mixed types (Int for years, AdPlaceholder for ads).
    // WHY: To seamlessly interleave native ads within the grid of year cards, matching
    //      the implementation on the main syllabus screen for a consistent user experience.
    val listItems = remember {
        val years = (1..6).toList()
        val adPositions = setOf(2, 4, 6) // Define where ads should appear
        val items = mutableListOf<Any>()
        years.forEachIndexed { index, year ->
            items.add(year)
            if (adPositions.contains(index + 1)) {
                items.add(AdPlaceholder(key = "ad_for_q_year_$year"))
            }
        }
        items
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // WHAT: Changed from `items` to a `forEach` loop with a `when` block.
        // WHY: To handle rendering different composables for different item types (YearCard vs Ad).
        listItems.forEach { item ->
            when (item) {
                is Int -> { // This is a year
                    item(key = "q_year_$item") {
                        YearCard(
                            year = item,
                            subjectCount = QuestionPaperRepository.getPapersForYear(item).size,
                            countLabel = "Papers",
                            onClick = { onYearClick(item) },
                            // WHAT: Added the sharedElement modifier.
                            // WHY: To enable the Material 3 transition animation when a user
                            //      clicks on a year card, creating a smooth visual connection.
                            modifier = Modifier
                                .sharedElement(
                                    rememberSharedContentState(key = "q-year-card-$item"),
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
