package `in`.shaaan.pcipharmd.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

// Data class to hold the specific information the Home Screen UI needs
data class YearInfo(val year: Int, val subjectCount: Int)

// Sealed interface to represent the different states of the Home Screen UI
sealed interface HomeScreenUiState {
    data class Success(val yearList: List<YearInfo>) : HomeScreenUiState
    data class Error(val message: String) : HomeScreenUiState
    data object Loading : HomeScreenUiState
}

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUiState>(HomeScreenUiState.Loading)
    val uiState: StateFlow<HomeScreenUiState> = _uiState

    init {
        loadHomeScreenData()
    }

    private fun loadHomeScreenData() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUiState.Loading
            fetchAllSyllabusData().collect { result ->
                result.fold(
                    onSuccess = { yearMap ->
                        val yearInfoList = yearMap.map { (year, data) ->
                            YearInfo(
                                year = year.toInt(),
                                subjectCount = data.subjects.size
                            )
                        }.sortedBy { it.year }
                        _uiState.value = HomeScreenUiState.Success(yearInfoList)
                    },
                    onFailure = {
                        _uiState.value = HomeScreenUiState.Error(it.message ?: "Failed to load data")
                    }
                )
            }
        }
    }

    private fun fetchAllSyllabusData(): Flow<Result<Map<String, SyllabusRepository.YearData>>> = callbackFlow {
        // WHAT: Explicitly provided the Realtime Database URL.
        // WHY: To fix the "permission denied" error by ensuring the app connects to the
        //      correct regional database instance where your ".read: true" rule is active.
        // HOW: Paste the URL from your Firebase Console into the `Firebase.database()` call.
        val databaseUrl = "https://pcipd-34920.firebaseio.com/" // <-- PASTE YOUR URL HERE
        val syllabusRef = Firebase.database(databaseUrl).reference.child("syllabus")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<Map<String, SyllabusRepository.YearData>>()
                if (data != null) {
                    trySend(Result.success(data))
                } else {
                    trySend(Result.failure(Exception("Syllabus data is empty or invalid.")))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        syllabusRef.addListenerForSingleValueEvent(listener)
        awaitClose { }
    }
}
