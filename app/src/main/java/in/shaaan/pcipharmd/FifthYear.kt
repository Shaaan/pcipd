package `in`.shaaan.pcipharmd

/**
 * Activity for the Fifth Year syllabus.
 * What: Displays subjects for the fifth year and handles navigation to their details.
 * Why: Provides a dedicated screen for fifth-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */

class FifthYear : BaseYearActivity() {

    companion object {
        private const val EPI_PATH = "fifth_year/epi"
        private const val RESEARCH_PATH = "fifth_year/research"
        private const val TDM_PATH = "fifth_year/tdm"
    }

    override val subjects = listOf(
        Pair(R.string.epi, EPI_PATH),
        Pair(R.string.research, RESEARCH_PATH),
        Pair(R.string.tdm, TDM_PATH)
    )

    override val adCount = 2
    override val adPositions = listOf(2, 4)
}
