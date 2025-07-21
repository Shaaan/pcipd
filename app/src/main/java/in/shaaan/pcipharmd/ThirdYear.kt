package `in`.shaaan.pcipharmd


/**
 * Activity for the Third Year syllabus.
 * What: Displays subjects for the third year and handles navigation to their details.
 * Why: Provides a dedicated screen for third-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class ThirdYear : BaseYearActivity(
) {
    companion object {
        private const val TP2_PATH = "third_year/tp2"
        private const val PHARMAC_PATH = "third_year/pharmac"
        private const val FORMULATION_PATH = "third_year/formulation"
        private const val ANALYSIS_PATH = "third_year/analysis"
        private const val JURIS_PATH = "third_year/juris"
        private const val MCHEM_PATH = "third_year/mchem"
    }

    override val subjects = listOf(
        Pair(R.string.tp2, TP2_PATH),
        Pair(R.string.pharmac, PHARMAC_PATH),
        Pair(R.string.formulation, FORMULATION_PATH),
        Pair(R.string.analysis, ANALYSIS_PATH),
        Pair(R.string.juris, JURIS_PATH),
        Pair(R.string.mchem, MCHEM_PATH)
    )

    override val adCount = 3
    override val adPositions = listOf(2, 4, 6)
}
