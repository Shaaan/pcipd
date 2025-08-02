package `in`.shaaan.pcipharmd

import android.app.Activity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.Subject
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.YearScreen(
    navController: NavController,
    year: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val viewModel: BaseYearViewModel = viewModel()
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    // Listen for one-time navigation events from the ViewModel (e.g., to show an ad)
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    // WHAT: Triggers the data fetch when the screen is first composed or if the year changes.
    // WHY: To load the syllabus data for the specific year as soon as the user navigates here.
    LaunchedEffect(key1 = year) {
        viewModel.fetchSyllabusForYear(year)
    }

    // WHAT: Collects the UI state from the ViewModel as a Compose State.
    // WHY: The UI will automatically recompose whenever the state changes (e.g., from Loading to Success).
    val uiState by viewModel.uiState.collectAsState()

    // WHAT: This `when` block acts as a state machine for the UI.
    // WHY: To show the correct UI for each possible state—loading spinner, error message,
    //      or the main content list—providing clear and immediate user feedback.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is YearUiState.Loading -> {
                CircularProgressIndicator()
            }
            is YearUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            is YearUiState.Success -> {
                // WHAT: On success, delegate the rendering of the list to a separate composable.
                // WHY: This keeps the main screen composable clean and focused on state management.
                SyllabusList(
                    year = year,
                    yearData = state.yearData,
                    navController = navController,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onSubjectClick = { subject ->
                        // The ViewModel handles the logic for opening the PDF, including showing an ad.
                        viewModel.onSubjectClicked(subject.endpoint)
                    },
                    onSubjectInfoClick = { subjectIndex ->
                        // Navigate to the detail screen for more info.
                        navController.navigate(Screen.SubjectDetail.createRoute(year, subjectIndex))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.SyllabusList(
    year: Int,
    yearData: SyllabusRepository.YearData,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSubjectClick: (Subject) -> Unit,
    onSubjectInfoClick: (Int) -> Unit
) {
    val subjects = yearData.subjects.values.toList()
    var listItems by remember { mutableStateOf<List<Any>>(emptyList()) }
    val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }

    // This effect creates the final list, interleaving subjects with ad placeholders.
    LaunchedEffect(yearData) {
        val items = mutableListOf<Any>()
        subjects.forEachIndexed { index, subject ->
            items.add(subject)
            if (yearData.adPositions.contains(index + 1)) {
                items.add(ListItem.AdItem)
            }
        }
        listItems = items

        // This triggers the staggered animation for list items appearing.
        listItems.forEachIndexed { index, item ->
            val key = if (item is Subject) item.title else "ad_$index"
            delay(50L)
            visibleItems[key] = true
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = listItems,
            key = { index, item -> if (item is Subject) item.title else "ad_$index" }
        ) { index, item ->
            val key = if (item is Subject) item.title else "ad_$index"
            val isVisible = visibleItems[key] ?: false
            val alpha by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, animationSpec = tween(300), label = "")
            val translationX by animateFloatAsState(targetValue = if (isVisible) 0f else -50f, animationSpec = tween(300), label = "")

            val itemModifier = Modifier
                .graphicsLayer {
                    this.alpha = alpha
                    this.translationX = translationX
                }
                .animateItem()

            when (item) {
                is Subject -> {
                    val subjectIndex = subjects.indexOf(item)
                    ListItem(
                        headlineContent = { Text(text = item.title) },
                        modifier = itemModifier
                            .padding(horizontal = 16.dp)
                            .clip(MaterialTheme.shapes.large)
                            .clickable {
                                // WHAT: Navigate to the detailed subject info screen.
                                // WHY: To provide richer content like key topics and books,
                                //      enhancing the app's value beyond just being a PDF link list.
                                onSubjectInfoClick(subjectIndex)
                            },
                        leadingContent = {
                            Icon(
                                Icons.Default.Book,
                                contentDescription = "Syllabus",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                        shadowElevation = 2.dp
                    )
                }

                is ListItem.AdItem -> {
                    Card(
                        modifier = itemModifier.padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                    }
                }
            }
        }
    }
}