package `in`.shaaan.pcipharmd

/**
 * Activity for the Sixth Year syllabus.
 * What: Displays subjects for the sixth year and handles navigation to their details.
 * Why: Provides a dedicated screen for sixth-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class SixthYear : BaseYearActivity(
) {
    companion object {
        private const val INTERN_ACTIVITIES_PATH = "sixth_year/"
        private const val INTERN_DOCUMENTS_PATH = "internship/"
    }


    override val subjects = listOf(
        Pair(R.string.intern_activities, INTERN_ACTIVITIES_PATH),
        Pair(R.string.intern_documents, INTERN_DOCUMENTS_PATH),

        )

    override val adCount = 2
    override val adPositions = listOf(2, 4)
}
