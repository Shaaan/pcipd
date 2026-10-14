package `in`.shaaan.pcipharmd.repository

import android.content.Context
import androidx.annotation.Keep
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.database
import `in`.shaaan.pcipharmd.models.Subject
import `in`.shaaan.pcipharmd.models.SyllabusTopicUnit
import `in`.shaaan.pcipharmd.models.SyllabusUnit
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

object SyllabusRepository {

    @Keep
    data class YearData(
        val subjects: List<Subject> = emptyList(),
        val adPositions: List<Int> = emptyList()
    )

    const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"

    private val memoryCache = ConcurrentHashMap<Int, YearData>()

    fun buildSyllabusUrl(endpoint: String): String {
        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint
        }

        val clean = endpoint.trim().trim('/')
        if (clean.isEmpty()) {
            return BASE_URL
        }

        return if (clean.endsWith("/")) {
            "$BASE_URL$clean"
        } else {
            "$BASE_URL$clean/"
        }
    }

    suspend fun getDataForYear(context: Context, year: Int): Result<YearData> {
        memoryCache[year]?.let { cachedData ->
            return Result.success(cachedData)
        }

        return try {
            val databaseUrl = "https://pcipd-34920.firebaseio.com/"
            val rootRef = Firebase.database(databaseUrl).reference

            val yearKey = when (year) {
                1 -> "first_year"
                2 -> "second_year"
                3 -> "third_year"
                4 -> "fourth_year"
                5 -> "fifth_year"
                6 -> "sixth_year"
                else -> year.toString()
            }

            // 1. Fetch directly from syllabus/$year (e.g. syllabus/1 for Year 1)
            var subjectsList = try {
                val yearSnap = rootRef.child("syllabus").child(year.toString()).get().await()
                parseSubjectsFromYearSnap(yearSnap)
            } catch (_: Exception) {
                emptyList()
            }

            // 2. If empty, check syllabus/first_year, second_year, etc.
            if (subjectsList.isEmpty()) {
                subjectsList = try {
                    val topSnap = rootRef.child("syllabus").get().await()
                    extractSubjectsFromNode(topSnap, year.toString(), yearKey)
                } catch (_: Exception) {
                    emptyList()
                }
            }

            // 3. Fallback to local asset
            if (subjectsList.isEmpty()) {
                subjectsList = loadYearSubjectsFromAsset(context, year)
            }

            if (subjectsList.isNotEmpty()) {
                val yearData = YearData(subjects = subjectsList)
                memoryCache[year] = yearData
                Result.success(yearData)
            } else {
                Result.failure(Exception("No syllabus data found for year $year."))
            }
        } catch (e: Exception) {
            val fallback = loadYearSubjectsFromAsset(context, year)
            if (fallback.isNotEmpty()) {
                val yearData = YearData(subjects = fallback)
                memoryCache[year] = yearData
                Result.success(yearData)
            } else {
                Result.failure(e)
            }
        }
    }

    fun loadYearSubjectsFromAsset(context: Context, year: Int): List<Subject> {
        return try {
            val jsonString = context.assets.open("syllabus_default.json").bufferedReader().use { it.readText() }
            val rootObj = JSONObject(jsonString)
            val targetObj = if (rootObj.has("syllabus")) rootObj.getJSONObject("syllabus") else rootObj

            val yearKey = year.toString()
            if (!targetObj.has(yearKey)) return emptyList()

            val yearObj = targetObj.getJSONObject(yearKey)
            if (!yearObj.has("subjects")) return emptyList()

            val subjectsArray = yearObj.getJSONArray("subjects")
            val list = mutableListOf<Subject>()

            for (i in 0 until subjectsArray.length()) {
                val subjObj = subjectsArray.getJSONObject(i)
                val title = subjObj.optString("title", "")
                val endpoint = subjObj.optString("endpoint", "")
                val theoryHours = subjObj.optString("theoryHours", "")
                val practicalHours = subjObj.optString("practicalHours", "")

                val booksList = mutableListOf<String>()
                if (subjObj.has("recommendedBooks")) {
                    val arr = subjObj.getJSONArray("recommendedBooks")
                    for (j in 0 until arr.length()) booksList.add(arr.getString(j))
                }

                val objsList = mutableListOf<String>()
                if (subjObj.has("objectives")) {
                    val arr = subjObj.getJSONArray("objectives")
                    for (j in 0 until arr.length()) objsList.add(arr.getString(j))
                }

                val topicsList = mutableListOf<String>()
                if (subjObj.has("keyTopics")) {
                    val arr = subjObj.getJSONArray("keyTopics")
                    for (j in 0 until arr.length()) topicsList.add(arr.getString(j))
                }

                val unitsList = mutableListOf<SyllabusUnit>()
                if (subjObj.has("units")) {
                    val arr = subjObj.getJSONArray("units")
                    for (j in 0 until arr.length()) {
                        val uObj = arr.getJSONObject(j)
                        unitsList.add(SyllabusUnit(uObj.optString("title"), uObj.optString("details")))
                    }
                }

                val theoryUnitsList = mutableListOf<SyllabusTopicUnit>()
                if (subjObj.has("theorySubjects")) {
                    val arr = subjObj.getJSONArray("theorySubjects")
                    for (j in 0 until arr.length()) {
                        val tuObj = arr.getJSONObject(j)
                        val tuTitle = tuObj.optString("title")
                        val dList = mutableListOf<String>()
                        if (tuObj.has("details")) {
                            val dArr = tuObj.getJSONArray("details")
                            for (k in 0 until dArr.length()) dList.add(dArr.getString(k))
                        }
                        theoryUnitsList.add(SyllabusTopicUnit(tuTitle, dList))
                    }
                }

                val practicalUnitsList = mutableListOf<SyllabusTopicUnit>()
                if (subjObj.has("practicalSubjects")) {
                    val arr = subjObj.getJSONArray("practicalSubjects")
                    for (j in 0 until arr.length()) {
                        val tuObj = arr.getJSONObject(j)
                        val tuTitle = tuObj.optString("title")
                        val dList = mutableListOf<String>()
                        if (tuObj.has("details")) {
                            val dArr = tuObj.getJSONArray("details")
                            for (k in 0 until dArr.length()) dList.add(dArr.getString(k))
                        }
                        practicalUnitsList.add(SyllabusTopicUnit(tuTitle, dList))
                    }
                }

                list.add(
                    Subject(
                        title = title,
                        endpoint = endpoint,
                        theoryHours = theoryHours,
                        practicalHours = practicalHours,
                        recommendedBooks = booksList,
                        objectives = objsList,
                        keyTopics = topicsList,
                        units = unitsList,
                        theorySubjects = theoryUnitsList,
                        practicalSubjects = practicalUnitsList
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun parseSubjectsFromYearSnap(yearSnap: DataSnapshot): List<Subject> {
        val list = mutableListOf<Subject>()
        if (!yearSnap.exists()) return list

        val targetNode = if (yearSnap.hasChild("subjects")) {
            yearSnap.child("subjects")
        } else {
            yearSnap
        }

        for (item in targetNode.children) {
            val subj = parseSingleSubject(item)
            if (subj != null) {
                list.add(subj)
            }
        }
        return list
    }

    private fun extractSubjectsFromNode(node: DataSnapshot, yearNum: String, yearKey: String): List<Subject> {
        val list = mutableListOf<Subject>()
        if (!node.exists()) return list

        for (child in node.children) {
            val key = child.key
            if (key == yearNum || key == yearKey) {
                parseSubjectsFromYearChild(child, list)
            } else if (child.hasChild(yearKey) || child.hasChild(yearNum)) {
                val nestedChild = if (child.hasChild(yearKey)) child.child(yearKey) else child.child(yearNum)
                parseSubjectsFromYearChild(nestedChild, list)
            }
        }
        return list
    }

    private fun parseSubjectsFromYearChild(yearChild: DataSnapshot, outList: MutableList<Subject>) {
        val targetNode = if (yearChild.hasChild("subjects")) yearChild.child("subjects") else yearChild
        for (item in targetNode.children) {
            val subj = parseSingleSubject(item)
            if (subj != null) {
                outList.add(subj)
            }
        }
    }

    private fun parseSingleSubject(snapshot: DataSnapshot): Subject? {
        if (snapshot.hasChild("title") || snapshot.hasChild("theorySubjects") || snapshot.hasChild("units") || snapshot.hasChild("keyTopics")) {
            return snapshot.getValue(Subject::class.java)
        }
        for (subChild in snapshot.children) {
            val s = subChild.getValue(Subject::class.java)
            if (s != null && (s.title.isNotBlank() || s.theorySubjects.isNotEmpty() || s.keyTopics.isNotEmpty())) {
                return s
            }
        }
        return snapshot.getValue(Subject::class.java)
    }
}
