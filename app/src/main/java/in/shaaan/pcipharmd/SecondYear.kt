package `in`.shaaan.pcipharmd

/**
 * Activity for the Second Year syllabus.
 * What: Displays subjects for the second year.
 * Why: Provides a dedicated screen for second-year content.
 * How: Extends `BaseYearActivity` and provides the necessary bindings and data maps. The base class handles all logic.
 */
class SecondYear : BaseYearActivity() {
    companion object {
        private const val COLOGY_PATH = "second_year/cology"
        private const val TP1_PATH = "second_year/tp1"
        private const val MICRO_PATH = "second_year/micro"
        private const val COGNOSY_PATH = "second_year/cognosy"
        private const val PATHO_PATH = "second_year/patho"
        private const val COM_PATH = "second_year/cmp"
    }

    override val subjects = listOf(
        Pair(R.string.cology, COLOGY_PATH),
        Pair(R.string.tp1, TP1_PATH),
        Pair(R.string.micro, MICRO_PATH),
        Pair(R.string.cognosy, COGNOSY_PATH),
        Pair(R.string.patho, PATHO_PATH),
        Pair(R.string.com, COM_PATH)
    )

    override val adCount = 3
    override val adPositions = listOf(2, 4, 6)
}
