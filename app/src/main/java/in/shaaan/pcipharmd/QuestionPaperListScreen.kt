package `in`.shaaan.pcipharmd.ui.screen

import android.app.Activity
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
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.ListItem
import `in`.shaaan.pcipharmd.NativeAdComposable
import `in`.shaaan.pcipharmd.QuestionPaperViewModel
import `in`.shaaan.pcipharmd.models.QuestionPaper
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuestionPaperListScreen(
    navController: NavController,
    year: Int,
    viewModel: QuestionPaperViewModel = viewModel(),
    // WHAT: Added scrollBehavior as a parameter.
    // WHY: To receive the scroll controller from the parent Scaffold in MainActivity.
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    var listItems by remember { mutableStateOf<List<Any>>(emptyList()) }
    val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    LaunchedEffect(key1 = year) {
        val papers = viewModel.getPapersForYear(year)
        val adPositions = setOf(2, 4, 6)
        val items = mutableListOf<Any>()

        papers.forEachIndexed { index, paper ->
            items.add(paper)
            if (adPositions.contains(index + 1)) {
                items.add(ListItem.AdItem)
            }
        }
        listItems = items

        listItems.forEachIndexed { index, item ->
            val key = when (item) {
                is QuestionPaper -> item.url
                is ListItem.AdItem -> "ad_qp_$index"
                else -> "unknown_qp_$index"
            }
            delay(50L)
            visibleItems[key] = true
        }
    }

    LazyColumn(
        // WHAT: Applied the nestedScroll modifier.
        // WHY: This connects the list's scrolling to the LargeTopAppBar, allowing it to expand and collapse.
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(
            items = listItems,
            key = { index, item ->
                when (item) {
                    is QuestionPaper -> item.url
                    is ListItem.AdItem -> "ad_qp_$index"
                    else -> "unknown_qp_$index"
                }
            }
        ) { index, item ->
            val key = when (item) {
                is QuestionPaper -> item.url
                is ListItem.AdItem -> "ad_qp_$index"
                else -> "unknown_qp_$index"
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
                is QuestionPaper -> {
                    ListItem(
                        headlineContent = { Text(item.title) },
                        modifier = itemModifier
                            .padding(horizontal = 16.dp)
                            .clip(MaterialTheme.shapes.large)
                            .clickable { viewModel.onPaperClicked(item) },
                        leadingContent = {
                            Icon(
                                Icons.Default.PictureAsPdf,
                                contentDescription = "PDF Icon",
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
