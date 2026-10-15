package `in`.shaaan.pcipharmd

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.shaaan.pcipharmd.models.Subject
import `in`.shaaan.pcipharmd.models.SubjectDetailUiState
import `in`.shaaan.pcipharmd.models.SubjectDetailViewModel
import `in`.shaaan.pcipharmd.models.SyllabusTopicUnit
import `in`.shaaan.pcipharmd.ui.components.ShimmerList

@Composable
fun SubjectDetailScreen(
    yearId: Int,
    subjectIndex: Int,
    detailViewModel: SubjectDetailViewModel = viewModel()
) {
    LaunchedEffect(yearId, subjectIndex) {
        detailViewModel.loadSubject(yearId, subjectIndex)
    }

    val uiState by detailViewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is SubjectDetailUiState.Loading -> ShimmerList(modifier = Modifier.fillMaxSize())
            is SubjectDetailUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error)
            is SubjectDetailUiState.Success -> {
                SubjectDetailContent(subject = state.subject)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubjectDetailContent(subject: Subject) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Subject Title & Teaching Hours Badges (Always Visible)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = subject.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (subject.theoryHours.isNotBlank() || subject.practicalHours.isNotBlank()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (subject.theoryHours.isNotBlank()) {
                            AssistChip(
                                onClick = { },
                                label = { Text("Theory: ${subject.theoryHours}") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Schedule,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                        if (subject.practicalHours.isNotBlank() && subject.practicalHours != "N/A") {
                            AssistChip(
                                onClick = { },
                                label = { Text("Practical: ${subject.practicalHours}") }
                            )
                        }
                    }
                }
            }
        }

        // 2. Recommended Books Section (Always Visible by default - uses bodyLarge)
        item {
            SectionHeader(title = "Recommended Books", icon = Icons.AutoMirrored.Filled.MenuBook)
            if (subject.recommendedBooks.isNotEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    subject.recommendedBooks.forEach { book ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 1.dp)
                            )
                            Text(
                                text = book,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            } else {
                Text(
                    "No recommended books available.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 3. Course Objectives Section (If present)
        if (subject.objectives.isNotEmpty()) {
            item {
                SectionHeader(title = "Course Objectives", icon = Icons.Default.CheckCircleOutline)
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    subject.objectives.forEach { obj ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Default.CheckCircleOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp).padding(top = 2.dp)
                            )
                            Text(
                                text = obj,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // 4. Expandable Card: Theory Syllabus (Collapsed by default)
        val theoryList = when {
            subject.theorySubjects.isNotEmpty() -> subject.theorySubjects
            subject.units.isNotEmpty() -> subject.units.map { SyllabusTopicUnit(it.title, if (it.details.isNotBlank()) listOf(it.details) else emptyList()) }
            else -> subject.keyTopics.map { SyllabusTopicUnit(it, emptyList()) }
        }

        if (theoryList.isNotEmpty()) {
            item {
                ExpandableSyllabusSection(
                    title = "Theory Syllabus",
                    icon = Icons.Default.Bookmark,
                    units = theoryList
                )
            }
        }

        // 5. Expandable Card: Practical Syllabus (Collapsed by default, if present)
        if (subject.practicalSubjects.isNotEmpty()) {
            item {
                ExpandableSyllabusSection(
                    title = "Practical Syllabus",
                    icon = Icons.Default.Science,
                    units = subject.practicalSubjects
                )
            }
        }
    }
}

@Composable
private fun ExpandableSyllabusSection(
    title: String,
    icon: ImageVector,
    units: List<SyllabusTopicUnit>
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (isExpanded) {
                MaterialTheme.colorScheme.surfaceContainer
            } else {
                MaterialTheme.colorScheme.surfaceContainerLow
            }
        )
    ) {
        Column {
            // Header Row: Only clicking here toggles expand/collapse
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "${units.size} units",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.outline
                )
            }

            if (isExpanded) {
                Column(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        units.forEachIndexed { idx, unit ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "•",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (unit.title.isNotBlank()) {
                                        Text(
                                            text = "${idx + 1}. ${unit.title}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    if (unit.details.isNotEmpty()) {
                                        Column(
                                            modifier = Modifier.padding(start = 8.dp, top = 2.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            unit.details.forEach { detailLine ->
                                                val cleanLine = detailLine.trim().removePrefix("•").trim()
                                                if (cleanLine.isNotBlank()) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.Top,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                    ) {
                                                        Text(
                                                            text = "◦",
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                                        )
                                                        Text(
                                                            text = cleanLine,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                            modifier = Modifier.weight(1f)
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
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
