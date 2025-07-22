// app/src/main/java/in/shaaan/pcipharmd/ui/screen/YearScreen.kt (New File)

package `in`.shaaan.pcipharmd.ui.screen

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.BaseYearViewModel
import `in`.shaaan.pcipharmd.ListItem
import `in`.shaaan.pcipharmd.SyllabusRepository
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
            TopAppBar(
                title = { Text("Year $year Syllabus") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        // What: A LazyColumn to display a vertical list.
        // Why: It's the Compose equivalent of a RecyclerView, efficiently rendering list items.
        // How: `items` iterates through our list of subjects and ads, rendering a different composable for each type.
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listItems) { item ->
                when (item) {
                    is ListItem.SubjectItem -> {
                        Button(
                            onClick = { viewModel.onSubjectClicked(context as Activity, item.endpoint) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = item.textResId))
                        }
                    }
                    is ListItem.AdItem -> {
                        NativeAdComposable(adUnitId = AdUtil.NATIVE_AD_UNIT_ID)
                    }
                }
            }
        }
    }
}