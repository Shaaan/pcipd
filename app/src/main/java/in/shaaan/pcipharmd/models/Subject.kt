package `in`.shaaan.pcipharmd.models

import androidx.annotation.Keep

/**
 * WHAT: A data class to represent a syllabus subject, compatible with Firebase.
 * WHY: Provides type safety for data fetched from the Realtime Database. The @Keep
 * annotation ensures that Proguard/R8 does not remove this class or its properties
 * during the build process, which would break Firebase's automatic data mapping.
 * Default values are required for the Firebase SDK's deserializer.
 */
@Keep
data class SyllabusTopicUnit(
    val title: String = "",
    val details: List<String> = emptyList()
)

@Keep
data class SyllabusUnit(
    val title: String = "",
    val details: String = ""
)

@Keep
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
