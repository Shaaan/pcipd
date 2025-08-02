package `in`.shaaan.pcipharmd.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

// Using the same YearInfo data class as HomeViewModel
// It holds the data the UI grid needs: the year and a count.
// data class YearInfo(val year: Int, val subjectCount: Int) is already defined in HomeViewModel.kt

// A specific UI state for this screen
sealed interface QuestionPapersUiState {
    data class Success(val yearList: List<YearInfo>) : QuestionPapersUiState
    data class Error(val message: String) : QuestionPapersUiState
    data object Loading : QuestionPapersUiState
}

class QuestionPapersViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<QuestionPapersUiState>(QuestionPapersUiState.Loading)
    val uiState: StateFlow<QuestionPapersUiState> = _uiState

    init {
        loadQuestionPaperData()
    }

    private fun loadQuestionPaperData() {
        viewModelScope.launch {
            _uiState.value = QuestionPapersUiState.Loading
            fetchAllQuestionPaperData().collect { result ->
                result.fold(
                    onSuccess = { paperMap ->
                        // WHAT: Transform the map from the database into a simple list for the UI.
                        // WHY: This prepares the data exactly as the UI needs it, simplifying the UI code.
                        val yearInfoList = paperMap.map { (year, papers) ->
                            YearInfo(
                                year = year.toInt(),
                                subjectCount = papers.size // Here the count is for papers
                            )
                        }.sortedBy { it.year }
                        _uiState.value = QuestionPapersUiState.Success(yearInfoList)
                    },
                    onFailure = {
                        _uiState.value = QuestionPapersUiState.Error(it.message ?: "Failed to load data")
                    }
                )
            }
        }
    }

    // WHAT: Fetches the entire "question_papers" node from RTDB at once.
    // WHY: This is highly efficient, minimizing network calls.
    private fun fetchAllQuestionPaperData(): Flow<Result<Map<String, List<QuestionPaper>>>> = callbackFlow {
        val papersRef = Firebase.database.reference.child("question_papers")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue<Map<String, List<QuestionPaper>>>()
                if (data != null) {
                    trySend(Result.success(data))
                } else {
                    // It's valid to have no papers, so send an empty map instead of an error.
                    trySend(Result.success(emptyMap()))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        papersRef.addListenerForSingleValueEvent(listener)
        awaitClose { }
    }
}
