package `in`.shaaan.pcipharmd


/**
 * Activity for the Fourth Year syllabus.
 * What: Displays subjects for the fourth year and handles navigation to their details.
 * Why: Provides a dedicated screen for fourth-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */
class FourthYear : BaseYearActivity() {
    companion object {
        private const val TP3_PATH = "fourth_year/pt3"
        private const val TOXICOLOGY_PATH = "fourth_year/toxicology"
        private const val CP_PATH = "fourth_year/cp"
        private const val HP_PATH = "fourth_year/hosp_pharm"
        private const val BIOPHARM_PATH = "fourth_year/biopharm"
        private const val BIOSTAT_PATH = "fourth_year/biostat"
    }

    override val subjects = listOf(
        Pair(R.string.tp3, TP3_PATH),
        Pair(R.string.toxicology, TOXICOLOGY_PATH),
        Pair(R.string.cp, CP_PATH),
        Pair(R.string.hp, HP_PATH),
        Pair(R.string.biopharm, BIOPHARM_PATH),
        Pair(R.string.biostat, BIOSTAT_PATH)
    )

    override val adCount = 3
    override val adPositions = listOf(2, 4, 6)
}
