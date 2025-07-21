package `in`.shaaan.pcipharmd


/**
 * Activity for the First Year syllabus.
 * What: Displays subjects for the first year and handles navigation to their details.
 * Why: Provides a dedicated screen for first-year content.
 * How: Extends `BaseYearActivity` to inherit common functionalities.
 */

class FirstYear : BaseYearActivity() {

    companion object {
        private const val HAP_PATH = "first_year/hap/"
        private const val CEUTICS_PATH = "first_year/pahrmaceutics/"
        private const val BIOCHEM_PATH = "first_year/biochem/"
        private const val IC_PATH = "first_year/ic/"
        private const val OC_PATH = "first_year/oc/"
        private const val REM_MATH_BIO_PATH = "first_year/math_bio/"
    }

    override val subjects = listOf(
        Pair(R.string.hap, HAP_PATH),
        Pair(R.string.ceutics, CEUTICS_PATH),
        Pair(R.string.biochem, BIOCHEM_PATH),
        Pair(R.string.oc, OC_PATH),
        Pair(R.string.ic, IC_PATH),
        Pair(R.string.rem_mathBio, REM_MATH_BIO_PATH)
    )

    override val adCount = 3
    override val adPositions = listOf(2, 4, 6)
}
