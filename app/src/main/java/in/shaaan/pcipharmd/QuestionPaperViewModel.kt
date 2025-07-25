// app/src/main/java/in/shaaan/pcipharmd/QuestionPaperViewModel.kt (Updated)
package `in`.shaaan.pcipharmd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
// WHAT: Corrected the import path for the QuestionPaper data class.
// WHY:  The model was located in the `in.shaaan.pcipharmd.models` package, but the
//       previous import was pointing to the root package. This fixes the compile error.
import `in`.shaaan.pcipharmd.models.QuestionPaper
import `in`.shaaan.pcipharmd.repository.QuestionPaperRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * WHAT: A ViewModel for the Question Papers feature.
 * WHY:  To decouple the UI from the data layer (QuestionPaperRepository), manage UI-related data,
 * and handle business logic, consistent with the app's architecture. This centralizes
 * the logic for fetching papers and handling user interactions.
 * HOW:  It exposes a SharedFlow for navigation events, similar to BaseYearViewModel, ensuring
 * a consistent pattern for handling actions that require context (like showing an ad
 * and opening a URL).
 */
class QuestionPaperViewModel(application: Application) : AndroidViewModel(application) {

    // A SharedFlow to emit navigation events (URL to open) to the UI.
    private val _navigationEvents = MutableSharedFlow<String>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    /**
     * Retrieves the list of question papers for a given year.
     * @param year The academic year.
     * @return A list of QuestionPaper objects.
     */
    fun getPapersForYear(year: Int): List<QuestionPaper> {
        return QuestionPaperRepository.getPapersForYear(year)
    }

    /**
     * Handles the click event on a question paper item.
     * It emits the URL to the navigationEvents flow, which the UI will observe.
     * @param paper The QuestionPaper object that was clicked.
     */
    fun onPaperClicked(paper: QuestionPaper) {
        viewModelScope.launch {
            _navigationEvents.emit(paper.url)
        }
    }
}
