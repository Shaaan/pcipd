package `in`.shaaan.pcipharmd

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun YearScreen(navController: NavController, year: Int) {
    val viewModel: BaseYearViewModel = viewModel()
    val context = LocalContext.current
    var listItems by remember { mutableStateOf<List<ListItem>>(emptyList()) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }

    LaunchedEffect(key1 = year) {
        val yearData = SyllabusRepository.getDataForYear(year)
        if (yearData != null) {
            val items = mutableListOf<ListItem>()
            yearData.subjects.forEachIndexed { index, subjectPair ->
                items.add(ListItem.SubjectItem(subjectPair.first, subjectPair.second))
                if (yearData.adPositions.contains(index + 1)) {
                    items.add(ListItem.AdItem)
                }
            }
            listItems = items

            listItems.forEachIndexed { index, item ->
                val key = when (item) {
                    is ListItem.SubjectItem -> item.textResId
                    is ListItem.AdItem -> "ad_$index"
                }
                delay(50L)
                visibleItems[key] = true
            }
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Year $year Syllabus") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }, scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = listItems,
                key = { index, item ->
                    when (item) {
                        is ListItem.SubjectItem -> item.textResId
                        is ListItem.AdItem -> "ad_$index"
                    }
                }
            ) { index, item ->
                val key = when (item) {
                    is ListItem.SubjectItem -> item.textResId
                    is ListItem.AdItem -> "ad_$index"
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
                    is ListItem.SubjectItem -> {
                        ListItem(
                            headlineContent = { Text(text = stringResource(id = item.textResId)) },
                            modifier = itemModifier
                                .padding(horizontal = 16.dp)
                                .clip(MaterialTheme.shapes.large)
                                .clickable { viewModel.onSubjectClicked(context as Activity, item.endpoint) },
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
}