package `in`.shaaan.pcipharmd.repository

import com.google.firebase.Firebase
import com.google.firebase.database.database
import `in`.shaaan.pcipharmd.models.QuestionPaper
import kotlinx.coroutines.tasks.await

object QuestionPaperRepository {

    const val BASE_URL = "https://pcipd.hypertex.co.in/q_papers/"

    fun buildQuestionPaperUrl(paperUrl: String): String {
        if (paperUrl.startsWith("http://") || paperUrl.startsWith("https://")) {
            return paperUrl
        }

        val clean = paperUrl.trim().trim('/')
        if (clean.isEmpty()) {
            return BASE_URL
        }

        return "$BASE_URL$clean"
    }

    suspend fun getPapersForYear(year: Int): Result<List<QuestionPaper>> {
        return try {
            val databaseUrl = "https://pcipd-34920.firebaseio.com/"
            val snapshot = Firebase.database(databaseUrl).reference.child("question_papers").child(year.toString()).get().await()
            val papers = snapshot.children.mapNotNull {
                it.getValue(QuestionPaper::class.java)
            }
            Result.success(papers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
