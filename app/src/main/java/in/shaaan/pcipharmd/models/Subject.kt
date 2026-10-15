package `in`.shaaan.pcipharmd.models

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable

/**
 * WHAT: Data classes to represent a syllabus subject, compatible with Firebase.
 * WHY: Provides type safety and Compose stability. The @Keep annotation prevents Proguard
 * from stripping constructors, and @Immutable informs the Compose compiler that properties
 * are immutable, enabling efficient recomposition skipping.
 */
@Keep
@Immutable
data class SyllabusTopicUnit(
    val title: String = "",
    val details: List<String> = emptyList()
)

@Keep
@Immutable
data class SyllabusUnit(
    val title: String = "",
    val details: String = ""
)

@Keep
@Immutable
data class Subject(
    val title: String = "",
    val endpoint: String = "",
    val theoryHours: String = "",
    val practicalHours: String = "",
    val recommendedBooks: List<String> = emptyList(),
    val theorySubjects: List<SyllabusTopicUnit> = emptyList(),
    val practicalSubjects: List<SyllabusTopicUnit> = emptyList(),
    val keyTopics: List<String> = emptyList(),
    val units: List<SyllabusUnit> = emptyList(),
    val objectives: List<String> = emptyList()
)
