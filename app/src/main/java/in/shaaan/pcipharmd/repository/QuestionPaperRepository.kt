package `in`.shaaan.pcipharmd.repository

import `in`.shaaan.pcipharmd.models.QuestionPaper

/**
 * WHAT: A repository to provide data for previous years' question papers.
 * WHY: To centralize data access for the new feature.
 * HOW: Uses a map to store lists of QuestionPaper objects for each academic year.
 * NOTE: The URLs are placeholders and should be replaced with actual links.
 */
object QuestionPaperRepository {

    private const val BASE_URL = "https://pcipd.hypertex.co.in/papers/"

    private val dataMap = mapOf(
        1 to listOf(
            QuestionPaper("First Year - Final Exam 2023", "${BASE_URL}1/2023.pdf"),
            QuestionPaper("First Year - Sessional 1 2023", "${BASE_URL}1/2023_s1.pdf"),
            QuestionPaper("First Year - Final Exam 2022", "${BASE_URL}1/2022.pdf"),
        ),
        2 to listOf(
            QuestionPaper("Second Year - Final Exam 2023", "${BASE_URL}2/2023.pdf"),
            QuestionPaper("Second Year - Final Exam 2022", "${BASE_URL}2/2022.pdf"),
            QuestionPaper("Second Year - Sessional 2 2022", "${BASE_URL}2/2022_s2.pdf"),
        ),
        3 to listOf(
            QuestionPaper("Third Year - Final Exam 2023", "${BASE_URL}3/2023.pdf"),
        ),
        4 to listOf(
            QuestionPaper("Fourth Year - Final Exam 2023", "${BASE_URL}4/2023.pdf"),
            QuestionPaper("Fourth Year - Final Exam 2022", "${BASE_URL}4/2022.pdf"),
        ),
        5 to listOf(
            QuestionPaper("Fifth Year - Final Exam 2023", "${BASE_URL}5/2023.pdf"),
        ),
        6 to emptyList() // No exams in the final internship year
    )

    fun getPapersForYear(year: Int): List<QuestionPaper> = dataMap[year] ?: emptyList()
}