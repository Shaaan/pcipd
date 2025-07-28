package `in`.shaaan.pcipharmd.ui.screen

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.BaseYearViewModel
import `in`.shaaan.pcipharmd.repository.SyllabusRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    yearId: Int,
    subjectIndex: Int,
    viewModel: BaseYearViewModel = viewModel()
) {
    val subject = SyllabusRepository.getDataForYear(yearId)?.subjects?.getOrNull(subjectIndex)
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    // Observe navigation events from the ViewModel to open the PDF
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    if (subject == null) {
        // Show an error or empty state if subject data is not found
        Text("Subject not found.")
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Key Topics Section
        item {
            Text(
                text = "Key Topics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (subject.keyTopics.isNotEmpty()) {
                        subject.keyTopics.forEach { topic ->
                            ListItem(
                                headlineContent = { Text(topic) },
                                leadingContent = { Icon(Icons.Default.Bookmark, contentDescription = null) }
                            )
                        }
                    } else {
                        Text("No key topics available for this subject.")
                    }
                }
            }
        }

        // Recommended Books Section
        item {
            Text(
                text = "Recommended Books",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (subject.recommendedBooks.isNotEmpty()) {
                        subject.recommendedBooks.forEach { book ->
                            ListItem(
                                headlineContent = { Text(book) },
                                leadingContent = { Icon(Icons.Default.MenuBook, contentDescription = null) }
                            )
                        }
                    } else {
                        Text("No recommended books available.")
                    }
                }
            }
        }

        // View Syllabus Button
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.onSubjectClicked(subject.endpoint) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.LibraryBooks,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "View Full Syllabus (PDF)")
            }
        }
    }
}
