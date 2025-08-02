package `in`.shaaan.pcipharmd.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

// WHAT: A sealed interface to represent the different states of the Subject Detail UI.
// WHY: Provides a type-safe, exhaustive way to handle loading, success, and error states
//      in the corresponding composable, preventing runtime errors and improving code clarity.
sealed interface SubjectDetailUiState {
    data class Success(val subject: Subject) : SubjectDetailUiState
    data class Error(val message: String) : SubjectDetailUiState
    data object Loading : SubjectDetailUiState
}

class SubjectDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SubjectDetailUiState>(SubjectDetailUiState.Loading)
    val uiState: StateFlow<SubjectDetailUiState> = _uiState

    // WHAT: A function to trigger loading the data for a specific subject.
    // WHY: This encapsulates the business logic for fetching and processing the data,
    //      keeping the UI layer clean and focused only on displaying the state.
    fun loadSubject(yearId: Int, subjectIndex: Int) {
        viewModelScope.launch {
            _uiState.value = SubjectDetailUiState.Loading
            SyllabusRepository.getDataForYear(yearId)
                .catch { e ->
                    _uiState.value = SubjectDetailUiState.Error(e.message ?: "An unknown error occurred")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { yearData ->
                            // WHAT: Safely get the subject from the list using its index.
                            // WHY: The data from Firebase is a map, so we convert it to a list first.
                            //      We handle the case where the index might be out of bounds.
                            val subject = yearData.subjects.values.toList().getOrNull(subjectIndex)
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
}
