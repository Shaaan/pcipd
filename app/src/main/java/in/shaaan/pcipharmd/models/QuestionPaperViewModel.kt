package `in`.shaaan.pcipharmd.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

// Using the same YearInfo data class as HomeViewModel
// It holds the data the UI grid needs: the year and a count.

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
                    onSuccess = { yearInfoList ->
                        _uiState.value = QuestionPapersUiState.Success(yearInfoList)
                    },
                    onFailure = {
                        _uiState.value = QuestionPapersUiState.Error(it.message ?: "Failed to load data")
                    }
                )
            }
        }
    }

    private fun fetchAllQuestionPaperData(): Flow<Result<List<YearInfo>>> = callbackFlow {
        val databaseUrl = "https://pcipd-34920.firebaseio.com/"
        val papersRef = Firebase.database(databaseUrl).reference.child("question_papers")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val yearInfoList = mutableListOf<YearInfo>()
                    for (child in snapshot.children) {
                        val yearKey = child.key?.toIntOrNull() ?: continue
                        val papers = child.children.mapNotNull { it.getValue(QuestionPaper::class.java) }
                        yearInfoList.add(YearInfo(year = yearKey, subjectCount = papers.size))
                    }
                    trySend(Result.success(yearInfoList.sortedBy { it.year }))
                } catch (e: Exception) {
                    trySend(Result.failure(e))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(Result.failure(error.toException()))
            }
        }
        papersRef.addListenerForSingleValueEvent(listener)
        awaitClose { papersRef.removeEventListener(listener) }
    }
}
