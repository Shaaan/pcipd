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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun SharedTransitionScope.YearScreen(navController: NavController, year: Int, animatedVisibilityScope: AnimatedVisibilityScope) {
    val viewModel: BaseYearViewModel = viewModel()
    val context = LocalContext.current
    var listItems by remember { mutableStateOf<List<Any>>(emptyList()) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val visibleItems = remember { mutableStateMapOf<Any, Boolean>() }

    // WHAT: Get the primary color from the Material 3 theme.
    // WHY: To pass to the AdUtil for a themed Custom Tab.
    // HOW: Using MaterialTheme.colorScheme.primary.toArgb().
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    // WHAT: A LaunchedEffect to listen for events from the ViewModel.
    // WHY: To handle navigation and other one-shot actions in the UI layer,
    //      decoupling it from the ViewModel.
    // HOW: It collects the 'navigationEvents' SharedFlow.
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            // The UI is now responsible for this action, passing the themed color.
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    LaunchedEffect(key1 = year) {
        val yearData = SyllabusRepository.getDataForYear(year)
        if (yearData != null) {
            val items = mutableListOf<Any>()
            yearData.subjects.forEachIndexed { index, subject ->
                items.add(subject) // Add the Subject object
                if (yearData.adPositions.contains(index + 1)) {
                    items.add(ListItem.AdItem) // Use a sealed class object for ads
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

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Year $year Syllabus") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }, scrollBehavior = scrollBehavior,
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = "year-card-$year"),
                    animatedVisibilityScope
                )
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
                        ListItem(
                            headlineContent = { Text(text = stringResource(id = item.nameResId)) },
                            modifier = itemModifier
                                .padding(horizontal = 16.dp)
                                .clip(MaterialTheme.shapes.large)
                                .clickable {
                                    // WHAT: The click listener no longer passes the Activity.
                                    // WHY: To decouple the UI from the ViewModel logic.
                                    // HOW: It now calls the updated onSubjectClicked with just the endpoint.
                                    viewModel.onSubjectClicked(item.endpoint)
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
}