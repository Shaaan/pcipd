package `in`.shaaan.pcipharmd.ui.screen

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.shaaan.pcipharmd.AdUtil
import `in`.shaaan.pcipharmd.BaseYearViewModel
import `in`.shaaan.pcipharmd.models.Subject
import `in`.shaaan.pcipharmd.models.SubjectDetailUiState
import `in`.shaaan.pcipharmd.models.SubjectDetailViewModel

@Composable
fun SubjectDetailScreen(
    yearId: Int,
    subjectIndex: Int,
    detailViewModel: SubjectDetailViewModel = viewModel(),
    baseViewModel: BaseYearViewModel = viewModel() // For showing ads on PDF click
) {
    // WHAT: Trigger the data fetch when the screen is first composed or its parameters change.
    // WHY: This ensures the correct subject data is loaded as soon as the user navigates here.
    LaunchedEffect(yearId, subjectIndex) {
        detailViewModel.loadSubject(yearId, subjectIndex)
    }

    val uiState by detailViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()

    // This observer is for the one-time event of opening the syllabus PDF.
    LaunchedEffect(Unit) {
        baseViewModel.navigationEvents.collect { url ->
            AdUtil.showInterAd(context as Activity, url, primaryColor)
        }
    }

    // WHAT: A state machine for the UI, similar to the other screens.
    // WHY: To provide clear user feedback by showing a loading indicator, an error message,
    //      or the main content when the data is successfully loaded.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is SubjectDetailUiState.Loading -> CircularProgressIndicator()
            is SubjectDetailUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is SubjectDetailUiState.Success -> {
                SubjectDetailContent(
                    subject = state.subject,
                    onViewSyllabusClick = {
                        baseViewModel.onSubjectClicked(state.subject.endpoint)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectDetailContent(subject: Subject, onViewSyllabusClick: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Subject Title
        item {
            Text(
                text = subject.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

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
                                leadingContent = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null) }
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
                onClick = onViewSyllabusClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.LibraryBooks,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "View Full Syllabus (PDF)")
            }
        }
    }
}
