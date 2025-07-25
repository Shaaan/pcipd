// app/src/main/java/in/shaaan/pcipharmd/ui/screen/QuestionPaperScreenList.kt (Updated)
package `in`.shaaan.pcipharmd.ui.screen

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.QuestionPaperViewModel

/**
 * WHAT: A screen to display the list of question papers for a specific year.
 * WHY: To allow users to select and view a specific paper.
 * HOW: It uses a LazyColumn to display ListItem composables. Clicking an item
 * now delegates the action to the QuestionPaperViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionPaperListScreen(
    navController: NavController,
    year: Int,
    // WHAT: Added ViewModel as a parameter with a default value.
    // WHY: To follow standard Jetpack Compose practice for providing ViewModels to screens.
    viewModel: QuestionPaperViewModel = viewModel()
) {
    // Data is now fetched from the ViewModel, not the repository directly.
    val papers = remember(year) { viewModel.getPapersForYear(year) }
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    // This LaunchedEffect block now observes navigation events from the ViewModel.
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            // The responsibility of showing the ad and opening the URL remains in the UI layer,
            // but the decision to navigate is made in the ViewModel.
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Year $year Papers") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(papers, key = { it.url }) { paper ->
                ListItem(
                    headlineContent = { Text(paper.title) },
                    modifier = Modifier.clickable {
                        // On click, we now notify the ViewModel.
                        viewModel.onPaperClicked(paper)
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.PictureAsPdf,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }
}
