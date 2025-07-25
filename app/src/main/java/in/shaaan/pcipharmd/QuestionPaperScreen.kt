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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import `in`.shaaan.pcipharmd.repository.QuestionPaperRepository
import `in`.shaaan.pcipharmd.YearCard

/**
 * WHAT: A screen to display the grid of years for selecting question papers.
 * WHY: To serve as the main entry point for the Question Papers feature.
 * HOW: It's a Scaffold containing a LazyVerticalStaggeredGrid of YearCards. It's
 * structurally similar to the HomeScreen for a consistent user experience.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.QuestionPapersScreen(
    onYearClick: (Int) -> Unit,
    onAboutClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val years = remember { (1..6).toList() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text("Previous Year Papers") },
                actions = {
                    IconButton(onClick = onAboutClick) {
                        Icon(Icons.Default.Info, contentDescription = "About")
                    }
                },
                scrollBehavior = scrollBehavior
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
            items(years.size, key = { "q_year_$it" }) { index ->
                val year = years[index]
                YearCard(
                    year = year,
                    subjectCount = QuestionPaperRepository.getPapersForYear(year).size,
                    countLabel = "Papers",
                    onClick = { onYearClick(year) },
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}