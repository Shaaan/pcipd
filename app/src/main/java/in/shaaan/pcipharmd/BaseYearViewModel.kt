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
import kotlinx.coroutines.launch

sealed interface YearUiState {
    data class Success(val yearData: SyllabusRepository.YearData) : YearUiState
    data class Error(val message: String) : YearUiState
    data object Loading : YearUiState
}

class BaseYearViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow<YearUiState>(YearUiState.Loading)
    val uiState: StateFlow<YearUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        AdUtil.loadInterAd(application)
    }

    fun fetchSyllabusForYear(year: Int) {
        viewModelScope.launch {
            _uiState.value = YearUiState.Loading
            val result = SyllabusRepository.getDataForYear(getApplication(), year)
            result.fold(
                onSuccess = { data -> _uiState.value = YearUiState.Success(data) },
                onFailure = { e -> _uiState.value = YearUiState.Error(e.message ?: "Failed to load data") }
            )
        }
    }

    fun onSubjectClicked(endpoint: String) {
        viewModelScope.launch {
            val url = SyllabusRepository.buildSyllabusUrl(endpoint)
            _navigationEvents.emit(url)
        }
    }
}
