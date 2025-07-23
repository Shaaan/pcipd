// app/src/main/java/in/shaaan/pcipharmd/ui/screen/YearScreen.kt (New File)

package `in`.shaaan.pcipharmd

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.ui.components.NativeAdComposable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearScreen(navController: NavController, year: Int) {
    // What: Get an instance of the ViewModel scoped to this screen.
    // Why: The ViewModel survives configuration changes and holds the business logic.
    // How: Using the `viewModel()` delegate from `lifecycle-viewmodel-compose`.
    val viewModel: BaseYearViewModel = viewModel()
    val context = LocalContext.current
    var listItems by remember { mutableStateOf<List<ListItem>>(emptyList()) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // What: A LaunchedEffect to load data when the screen is first composed.
    // Why: This is the correct way to call suspend functions or load data asynchronously in response to a state change (in this case, the initial composition).
    // How: It runs the block of code once when `year` changes.
    LaunchedEffect(key1 = year) {
        val yearData = SyllabusRepository.getDataForYear(year)
        if (yearData != null) {
            val items = mutableListOf<ListItem>()
            yearData.subjects.forEachIndexed { index, subjectPair ->
                items.add(ListItem.SubjectItem(subjectPair.first, subjectPair.second))
                if (yearData.adPositions.contains(index + 1)) {
                    // We use a placeholder here; the AdComposable will handle the actual loading.
                    items.add(ListItem.AdItem)
                }
            }
            listItems = items
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
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(
                items = listItems,
                key = { index, item ->
                    when (item) {
                        is ListItem.SubjectItem -> item.textResId // Stable and unique
                        is ListItem.AdItem -> "ad_$index" // Guaranteed unique for each ad slot
                    }
                }
            ) { index, item -> // The lambda now provides both index and item
                when (item) {
                    is ListItem.SubjectItem -> {
                        ListItem(
                            headlineContent = { Text(stringResource(id = item.textResId)) },
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { viewModel.onSubjectClicked(context as Activity, item.endpoint) }
                                .animateItem(), // Add for smooth reordering animations
                            leadingContent = {
                                Icon(
                                    Icons.Default.Book,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                    is ListItem.AdItem -> {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .animateItem(), // Add for smooth reordering animations
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onPrimaryContainer
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