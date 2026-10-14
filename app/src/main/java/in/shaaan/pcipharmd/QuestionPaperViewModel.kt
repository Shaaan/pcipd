package `in`.shaaan.pcipharmd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.shaaan.pcipharmd.models.QuestionPaper
import `in`.shaaan.pcipharmd.repository.QuestionPaperRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface QuestionPaperListUiState {
    data class Success(val papers: List<QuestionPaper>) : QuestionPaperListUiState
    data class Error(val message: String) : QuestionPaperListUiState
    data object Loading : QuestionPaperListUiState
}

class QuestionPaperViewModel(application: Application) : AndroidViewModel(application) {

    private val _listUiState = MutableStateFlow<QuestionPaperListUiState>(QuestionPaperListUiState.Loading)
    val listUiState: StateFlow<QuestionPaperListUiState> = _listUiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun fetchPapersForYear(year: Int) {
        viewModelScope.launch {
            _listUiState.value = QuestionPaperListUiState.Loading
            val result = QuestionPaperRepository.getPapersForYear(year)
            result.fold(
                onSuccess = { papers -> _listUiState.value = QuestionPaperListUiState.Success(papers) },
                onFailure = { e -> _listUiState.value = QuestionPaperListUiState.Error(e.message ?: "Failed to load") }
            )
        }
    }

    fun onPaperClicked(paper: QuestionPaper) {
        viewModelScope.launch {
            val fullUrl = QuestionPaperRepository.buildQuestionPaperUrl(paper.url)
            _navigationEvents.emit(fullUrl)
        }
    }
}
