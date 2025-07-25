package `in`.shaaan.pcipharmd.models

import androidx.annotation.StringRes

/**
 * WHAT: A new data class to represent a syllabus subject.
 * WHY: Provides better type safety and code readability than using a generic Pair.
 * It makes the purpose of the data explicit.
 * HOW: Defined with clear property names 'nameResId' and 'endpoint'.
 */
data class Subject(@StringRes val nameResId: Int, val endpoint: String)
