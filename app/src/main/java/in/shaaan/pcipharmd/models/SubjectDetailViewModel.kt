package `in`.shaaan.pcipharmd.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface SubjectDetailUiState {
    data class Success(val subject: Subject) : SubjectDetailUiState
    data class Error(val message: String) : SubjectDetailUiState
    data object Loading : SubjectDetailUiState
}

class SubjectDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<SubjectDetailUiState>(SubjectDetailUiState.Loading)
    val uiState: StateFlow<SubjectDetailUiState> = _uiState

    fun loadSubject(yearId: Int, subjectIndex: Int) {
        viewModelScope.launch {
            _uiState.value = SubjectDetailUiState.Loading
            val result = SyllabusRepository.getDataForYear(getApplication(), yearId)
            result.fold(
                onSuccess = { yearData ->
                    val subject = yearData.subjects.getOrNull(subjectIndex)
                    if (subject != null) {
                        _uiState.value = SubjectDetailUiState.Success(subject)
                    } else {
                        _uiState.value = SubjectDetailUiState.Error("Subject not found at index $subjectIndex.")
                    }
                },
                onFailure = { e ->
                    _uiState.value = SubjectDetailUiState.Error(e.message ?: "Failed to load data")
                }
            )
        }
    }
}
