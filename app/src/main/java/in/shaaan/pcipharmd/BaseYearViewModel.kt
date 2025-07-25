package `in`.shaaan.pcipharmd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import `in`.shaaan.pcipharmd.repository.SyllabusRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BaseYearViewModel(application: Application) : AndroidViewModel(application) {

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

    fun onSubjectClicked(endpoint: String) {
        viewModelScope.launch {
            val url = "${SyllabusRepository.BASE_URL}$endpoint"
            _navigationEvents.emit(url)
        }
    }
}
