package `in`.shaaan.pcipharmd

object SyllabusRepository {

    // What: A constant key for the Intent extra.
    // Why: Using a constant prevents typos and ensures the key for sending/receiving data via
    //      Intents is consistent throughout the app.
    const val EXTRA_YEAR = "in.shaaan.pcipharmd.EXTRA_YEAR"
    const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"

    // What: A data class to hold all information related to a specific year.
    // Why: Encapsulates related data points (subjects and ad positions) into a single, clean object.
    data class YearData(val subjects: List<Pair<Int, String>>, val adPositions: List<Int>)

    // What: A map holding all syllabus data for every year.
    // Why: This is the core of the data centralization. It replaces the individual lists that were
    //      defined in each of the six separate Year activity files.
    private val dataMap = mapOf(
        1 to YearData(
            subjects = listOf(
                Pair(R.string.hap, "first_year/hap/"),
                Pair(R.string.ceutics, "first_year/pahrmaceutics/"),
                Pair(R.string.biochem, "first_year/biochem/"),
                Pair(R.string.oc, "first_year/oc/"),
                Pair(R.string.ic, "first_year/ic/"),
                Pair(R.string.rem_mathBio, "first_year/math_bio/")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        2 to YearData(
            subjects = listOf(
                Pair(R.string.cology, "second_year/cology"),
                Pair(R.string.tp1, "second_year/tp1"),
                Pair(R.string.micro, "second_year/micro"),
                Pair(R.string.cognosy, "second_year/cognosy"),
                Pair(R.string.patho, "second_year/patho"),
                Pair(R.string.com, "second_year/cmp")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        3 to YearData(
            subjects = listOf(
                Pair(R.string.tp2, "third_year/tp2"),
                Pair(R.string.pharmac, "third_year/pharmac"),
                Pair(R.string.formulation, "third_year/formulation"),
                Pair(R.string.analysis, "third_year/analysis"),
                Pair(R.string.juris, "third_year/juris"),
                Pair(R.string.mchem, "third_year/mchem")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        4 to YearData(
            subjects = listOf(
                Pair(R.string.tp3, "fourth_year/pt3"),
                Pair(R.string.toxicology, "fourth_year/toxicology"),
                Pair(R.string.cp, "fourth_year/cp"),
                Pair(R.string.hp, "fourth_year/hosp_pharm"),
                Pair(R.string.biopharm, "fourth_year/biopharm"),
                Pair(R.string.biostat, "fourth_year/biostat")
            ),
            adPositions = listOf(2, 4, 6)
        ),
        5 to YearData(
            subjects = listOf(
                Pair(R.string.epi, "fifth_year/epi"),
                Pair(R.string.research, "fifth_year/research"),
                Pair(R.string.tdm, "fifth_year/tdm")
            ),
            adPositions = listOf(2, 4)
        ),
        6 to YearData(
            subjects = listOf(
                Pair(R.string.intern_activities, "sixth_year/"),
                Pair(R.string.intern_documents, "internship/")
            ),
            adPositions = listOf(2, 4)
        )
    )

    // What: A public function to safely retrieve data for a given year.
    // Why: Provides a controlled access point to the data map, returning a nullable `YearData`
    //      object, which forces the caller to handle the case where an invalid year is requested.
    fun getDataForYear(year: Int): YearData? = dataMap[year]
}