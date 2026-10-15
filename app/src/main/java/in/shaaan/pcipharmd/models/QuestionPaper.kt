package `in`.shaaan.pcipharmd.models

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable

/**
 * WHAT: A data class to represent a single question paper.
 * WHY: To provide a clear, type-safe model for question paper data.
 * HOW: Defined with properties for the paper's title and its URL.
 */
@Keep
@Immutable
data class QuestionPaper(
    val title: String = "",
    val url: String = ""
)
