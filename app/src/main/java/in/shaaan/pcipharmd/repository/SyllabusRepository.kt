package `in`.shaaan.pcipharmd.repository

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import `in`.shaaan.pcipharmd.models.Subject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object SyllabusRepository {

    data class YearData(
        val subjects: Map<String, Subject> = emptyMap(),
        val adPositions: List<Int> = emptyList()
    )

    const val BASE_URL = "https://pcipd.hypertex.co.in/syllabus/"

    fun getDataForYear(year: Int): Flow<Result<YearData>> = callbackFlow {
        // WHAT: Explicitly provided the Realtime Database URL.
        // WHY: To fix the "permission denied" error by ensuring the app connects to the
        //      correct regional database instance where your ".read: true" rule is active.
        // HOW: Paste the URL from your Firebase Console into the `Firebase.database()` call.
        val databaseUrl = "https://pcipd-34920.firebaseio.com/" // <-- PASTE YOUR URL HERE
        val yearRef = Firebase.database(databaseUrl).reference.child("pharmd").child("syllabus").child(year.toString())

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val yearData = snapshot.getValue(YearData::class.java)
                if (yearData != null) {
                    trySend(Result.success(yearData))
                } else {
                    trySend(Result.failure(Exception("No data found for year $year.")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        yearRef.addValueEventListener(listener)

        awaitClose {
            yearRef.removeEventListener(listener)
        }
    }
}
