package `in`.shaaan.pcipharmd.models

/**
 * WHAT: A data class to represent a single question paper.
 * WHY: To provide a clear, type-safe model for question paper data.
 * HOW: Defined with properties for the paper's title and its URL.
 */
data class QuestionPaper(
    val title: String = "",
    val url: String = ""
)