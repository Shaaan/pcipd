package `in`.shaaan.pcipharmd.models

import androidx.annotation.Keep

/**
 * WHAT: A data class to represent a syllabus subject, compatible with Firebase.
 * WHY: Provides type safety for data fetched from the Realtime Database. The @Keep
 * annotation ensures that Proguard/R8 does not remove this class or its properties
 * during the build process, which would break Firebase's automatic data mapping.
 * Default values are required for the Firebase SDK's deserializer.
 * HOW: Defined with properties that match the keys in your Firebase JSON structure.
 */
@Keep // Ensures Proguard doesn't strip this class
data class Subject(
    val title: String = "",
    val endpoint: String = "",
    val keyTopics: List<String> = emptyList(),
    val recommendedBooks: List<String> = emptyList()
)
