package `in`.shaaan.pcipharmd

import android.app.Activity
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.Subject
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import `in`.shaaan.pcipharmd.ui.components.ShimmerList
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.YearScreen(
    navController: NavController,
    year: Int,
    animatedVisibilityScope: AnimatedVisibilityScope,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val viewModel: BaseYearViewModel = viewModel()
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    LaunchedEffect(key1 = year) {
        viewModel.fetchSyllabusForYear(year)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is YearUiState.Loading -> {
                ShimmerList(modifier = Modifier.fillMaxSize())
            }
            is YearUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            is YearUiState.Success -> {
                SyllabusList(
                    yearData = state.yearData,
                    scrollBehavior = scrollBehavior,
                    onSubjectInfoClick = { subjectIndex ->
                        navController.navigate(Screen.SubjectDetail.createRoute(year, subjectIndex))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SyllabusList(
    yearData: SyllabusRepository.YearData,
    scrollBehavior: TopAppBarScrollBehavior,
    onSubjectInfoClick: (Int) -> Unit
) {
    val subjects = yearData.subjects
    var isAdFailed by remember { mutableStateOf(false) }
    var listItems by remember { mutableStateOf<List<Any>>(emptyList()) }
    val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }

    LaunchedEffect(yearData, isAdFailed) {
        val items = mutableListOf<Any>()
        subjects.forEachIndexed { index, subject ->
            items.add(subject)
            if (index == 1 && !isAdFailed) {
                items.add(ListItem.AdItem)
            }
        }
        listItems = items

        listItems.forEachIndexed { index, item ->
            val key = if (item is Subject) item.title else "ad_$index"
            delay(50L)
            visibleItems[key] = true
        }
    }

    LazyColumn(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
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
                        headlineContent = { 
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        modifier = itemModifier
                            .padding(horizontal = 4.dp)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .clickable {
                                onSubjectInfoClick(subjectIndex)
                            },
                        leadingContent = {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Icon(
                                    Icons.Default.Book,
                                    contentDescription = "Syllabus",
                                    modifier = Modifier.padding(8.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        trailingContent = {
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                        tonalElevation = 2.dp,
                        shadowElevation = 1.dp
                    )
                }

                is ListItem.AdItem -> {
                    Card(
                        modifier = itemModifier.padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
