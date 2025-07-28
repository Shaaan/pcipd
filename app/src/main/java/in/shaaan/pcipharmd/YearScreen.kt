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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.models.Subject
import `in`.shaaan.pcipharmd.navigation.Screen
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.delay

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SharedTransitionScope.YearScreen(
    navController: NavController,
    year: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val viewModel: BaseYearViewModel = viewModel()
    val context = LocalContext.current
    var listItems by remember { mutableStateOf<List<Any>>(emptyList()) }
    val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    LaunchedEffect(key1 = year) {
        val yearData = SyllabusRepository.getDataForYear(year)
        if (yearData != null) {
            val items = mutableListOf<Any>()
            yearData.subjects.forEachIndexed { index, subject ->
                items.add(subject)
                if (yearData.adPositions.contains(index + 1)) {
                    items.add(ListItem.AdItem)
                }
            }
            listItems = items

            listItems.forEachIndexed { index, item ->
                val key = when (item) {
                    is Subject -> item.nameResId
                    is ListItem.AdItem -> "ad_$index"
                    else -> "unknown_$index"
                }
                delay(50L)
                visibleItems[key] = true
            }
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = listItems,
            key = { index, item ->
                when (item) {
                    is Subject -> item.nameResId
                    is ListItem.AdItem -> "ad_$index"
                    else -> "unknown_$index"
                }
            }
        ) { index, item ->
            val key = when (item) {
                is Subject -> item.nameResId
                is ListItem.AdItem -> "ad_$index"
                else -> "unknown_$index"
            }
            val isVisible = visibleItems[key] ?: false

            val alpha by animateFloatAsState(
                targetValue = if (isVisible) 1f else 0f,
                animationSpec = tween(300),
                label = "ItemAlpha"
            )
            val translationX by animateFloatAsState(
                targetValue = if (isVisible) 0f else -50f,
                animationSpec = tween(300),
                label = "ItemTranslationX"
            )

            val itemModifier = Modifier
                .graphicsLayer {
                    this.alpha = alpha
                    this.translationX = translationX
                }
                .animateItem()

            when (item) {
                is Subject -> {
                    // WHAT: Find the original index of the subject within the subjects-only list.
                    // WHY: The `itemsIndexed` index includes ads, but we need the index
                    //      relative to the original `subjects` list to pass to the detail screen.
                    val subjectIndex = SyllabusRepository.getDataForYear(year)?.subjects?.indexOf(item) ?: -1

                    ListItem(
                        headlineContent = { Text(text = stringResource(id = item.nameResId)) },
                        modifier = itemModifier
                            .padding(horizontal = 16.dp)
                            .clip(MaterialTheme.shapes.large)
                            .clickable {
                                // WHAT: Changed the click action to navigate to the SubjectDetail screen.
                                // WHY: To show the new content-rich screen instead of opening the PDF directly.
                                if (subjectIndex != -1) {
                                    navController.navigate(
                                        Screen.SubjectDetail.createRoute(year, subjectIndex)
                                    )
                                }
                            },
                        leadingContent = {
                            Icon(
                                Icons.Default.Book,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                        ),
                        shadowElevation = 2.dp
                    )
                }

                is ListItem.AdItem -> {
                    Card(
                        modifier = itemModifier.padding(horizontal = 16.dp),
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
