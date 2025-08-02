package `in`.shaaan.pcipharmd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

sealed interface YearUiState {
    data class Success(val yearData: SyllabusRepository.YearData) : YearUiState
    data class Error(val message: String) : YearUiState
    data object Loading : YearUiState
}

class BaseYearViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<YearUiState>(YearUiState.Loading)
    val uiState: StateFlow<YearUiState> = _uiState.asStateFlow()

    /**
     * WHAT: A SharedFlow to emit one-time events from the ViewModel to the UI.
     * WHY: To decouple the ViewModel from the View (Activity/Composable). The ViewModel
     * should not know about Android UI components. This prevents memory leaks and
     * adheres to modern MVVM architecture.
     * HOW: The ViewModel emits a URL string. The UI collects this flow and handles
     * the action (showing the ad and opening the Custom Tab).
     */
    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        AdUtil.loadInterAd(application)
    }

    fun fetchSyllabusForYear(year: Int) {
        viewModelScope.launch {
            _uiState.value = YearUiState.Loading
            SyllabusRepository.getDataForYear(year)
                .catch { e -> _uiState.value = YearUiState.Error(e.message ?: "An unknown error occurred") }
                .collect { result ->
                    result.fold(
                        onSuccess = { data -> _uiState.value = YearUiState.Success(data) },
                        onFailure = { e -> _uiState.value = YearUiState.Error(e.message ?: "Failed to load data") }
                    )
                }
        }
    }

    fun onSubjectClicked(endpoint: String) {
        viewModelScope.launch {
            val url = "${SyllabusRepository.BASE_URL}$endpoint"
            _navigationEvents.emit(url)
        }
    }
}
