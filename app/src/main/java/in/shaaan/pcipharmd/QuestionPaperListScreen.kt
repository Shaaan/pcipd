package `in`.shaaan.pcipharmd

import android.app.Activity
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.shaaan.pcipharmd.models.QuestionPaper
import `in`.shaaan.pcipharmd.ui.components.ShimmerList
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionPaperListScreen(
    year: Int,
    viewModel: QuestionPaperViewModel = viewModel(),
    scrollBehavior: TopAppBarScrollBehavior
) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val uiState by viewModel.listUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    LaunchedEffect(key1 = year) {
        viewModel.fetchPapersForYear(year)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is QuestionPaperListUiState.Loading -> {
                ShimmerList(modifier = Modifier.fillMaxSize())
            }
            is QuestionPaperListUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            is QuestionPaperListUiState.Success -> {
                val papers = state.papers
                var isAdFailed by remember { mutableStateOf(false) }
                val listItems = remember(papers, isAdFailed) {
                    val items = mutableListOf<Any>()
                    papers.forEachIndexed { index, paper ->
                        items.add(paper)
                        if (index == 1 && !isAdFailed) {
                            items.add(ListItem.AdItem)
                        }
                    }
                    items
                }
                val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }

                LaunchedEffect(listItems) {
                    listItems.forEachIndexed { index, item ->
                        val key = if (item is QuestionPaper) item.url else "ad_$index"
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
                        key = { index, item -> if (item is QuestionPaper) item.url else "ad_$index" }
                    ) { index, item ->
                        val key = if (item is QuestionPaper) item.url else "ad_$index"
                        val isVisible = visibleItems[key] ?: false

                        val alpha by animateFloatAsState(targetValue = if (isVisible) 1f else 0f, label = "")
                        val translationX by animateFloatAsState(targetValue = if (isVisible) 0f else -50f, label = "")

                        val itemModifier = Modifier
                            .graphicsLayer {
                                this.alpha = alpha
                                this.translationX = translationX
                            }
                            .animateItem()

                        when (item) {
                            is QuestionPaper -> {
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
                                        .clickable { viewModel.onPaperClicked(item) },
                                    leadingContent = {
                                        Surface(
                                            modifier = Modifier.size(40.dp),
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                        ) {
                                            Icon(
                                                Icons.Default.PictureAsPdf,
                                                contentDescription = null,
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
                                    colors = ListItemDefaults.colors(
                                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                    ),
                                    tonalElevation = 2.dp,
                                    shadowElevation = 1.dp
                                )
                            }
                            is ListItem.AdItem -> {
                                Card(
                                    modifier = itemModifier.padding(horizontal = 4.dp),
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
        }
    }
}
