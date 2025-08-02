// app/src/main/java/in/shaaan/pcipharmd/repository/QuestionPaperRepository.kt
package `in`.shaaan.pcipharmd.repository

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import `in`.shaaan.pcipharmd.models.QuestionPaper
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * WHAT: A repository to provide data for previous years' question papers from Firebase RTDB.
 * WHY: To centralize and abstract the data source, making the app's content dynamic
 * and manageable from the cloud without requiring an app update.
 * HOW: It uses a callbackFlow to wrap Firebase's asynchronous listener, emitting
 * a Result object that represents either the successfully fetched data or an error.
 */
object QuestionPaperRepository {

    private val database = Firebase.database.reference.child("question_papers")

    fun getPapersForYear(year: Int): Flow<Result<List<QuestionPaper>>> = callbackFlow {
        val yearRef = database.child(year.toString())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // WHAT: Iterate through the children of the snapshot and map them to QuestionPaper objects.
                // WHY: The data in RTDB is a list, so we must process each child node individually.
                //      `getValue(QuestionPaper::class.java)` automatically converts a node into our data class.
                val papers = snapshot.children.mapNotNull {
                    it.getValue(QuestionPaper::class.java)
                }
                // Emitting a success Result with the list of papers.
                trySend(Result.success(papers))
            }

            override fun onCancelled(error: DatabaseError) {
                // Emitting a failure Result if the database call is cancelled or fails.
                trySend(Result.failure(error.toException()))
            }
        }
        yearRef.addValueEventListener(listener)

        // WHAT: This block is crucial for cleaning up the listener.
        // WHY: When the collecting coroutine is cancelled, this removes the ValueEventListener
        //      to prevent memory leaks and unnecessary background operations.
        awaitClose {
            yearRef.removeEventListener(listener)
        }
    }
}