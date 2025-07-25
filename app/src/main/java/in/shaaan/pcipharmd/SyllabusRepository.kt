package `in`.shaaan.pcipharmd

import `in`.shaaan.pcipharmd.models.Subject

object SyllabusRepository {

    const val EXTRA_YEAR = "in.shaaan.pcipharmd.EXTRA_YEAR"
    const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"

    /**
     * WHAT: The data structure for a year's data.
     * WHY: To provide a clear, structured model for the syllabus.
     * HOW: Now uses a List of the new 'Subject' data class instead of List<Pair<Int, String>>.
     */
    data class YearData(val subjects: List<Subject>, val adPositions: List<Int>)

    private val dataMap = mapOf(
        1 to YearData(
            subjects = listOf(
                Subject(R.string.hap, "first_year/hap/"),
                Subject(R.string.ceutics, "first_year/pahrmaceutics/"),
                Subject(R.string.biochem, "first_year/biochem/"),
                Subject(R.string.oc, "first_year/oc/"),
                Subject(R.string.ic, "first_year/ic/"),
                Subject(R.string.rem_mathBio, "first_year/math_bio/")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        2 to YearData(
            subjects = listOf(
                Subject(R.string.cology, "second_year/cology"),
                Subject(R.string.tp1, "second_year/tp1"),
                Subject(R.string.micro, "second_year/micro"),
                Subject(R.string.cognosy, "second_year/cognosy"),
                Subject(R.string.patho, "second_year/patho"),
                Subject(R.string.com, "second_year/cmp")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        3 to YearData(
            subjects = listOf(
                Subject(R.string.tp2, "third_year/tp2"),
                Subject(R.string.pharmac, "third_year/pharmac"),
                Subject(R.string.formulation, "third_year/formulation"),
                Subject(R.string.analysis, "third_year/analysis"),
                Subject(R.string.juris, "third_year/juris"),
                Subject(R.string.mchem, "third_year/mchem")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        4 to YearData(
            subjects = listOf(
                Subject(R.string.tp3, "fourth_year/pt3"),
                Subject(R.string.toxicology, "fourth_year/toxicology"),
                Subject(R.string.cp, "fourth_year/cp"),
                Subject(R.string.hp, "fourth_year/hosp_pharm"),
                Subject(R.string.biopharm, "fourth_year/biopharm"),
                Subject(R.string.biostat, "fourth_year/biostat")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        5 to YearData(
            subjects = listOf(
                Subject(R.string.epi, "fifth_year/epi"),
                Subject(R.string.research, "fifth_year/research"),
                Subject(R.string.tdm, "fifth_year/tdm")
            ),
            adPositions = listOf(2, 4)
        ),
        6 to YearData(
            subjects = listOf(
                Subject(R.string.intern_activities, "sixth_year/"),
                Subject(R.string.intern_documents, "internship/")
            ),
            adPositions = listOf(2, 4)
        )
    )

    fun getDataForYear(year: Int): YearData? = dataMap[year]
}