package `in`.shaaan.pcipharmd.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

// Data class to hold the specific information the Home Screen UI needs
data class YearInfo(val year: Int, val subjectCount: Int)

// Sealed interface to represent the different states of the Home Screen UI
sealed interface HomeScreenUiState {
    data class Success(val yearList: List<YearInfo>) : HomeScreenUiState
    data class Error(val message: String) : HomeScreenUiState
    data object Loading : HomeScreenUiState
}

class HomeViewModel(application: Application) : AndroidViewModel(application) {

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
                    onSuccess = { yearInfoList ->
                        _uiState.value = HomeScreenUiState.Success(yearInfoList)
                    },
                    onFailure = {
                        _uiState.value = HomeScreenUiState.Error(it.message ?: "Failed to load data")
                    }
                )
            }
        }
    }

    private fun fetchAllSyllabusData(): Flow<Result<List<YearInfo>>> = callbackFlow {
        val databaseUrl = "https://pcipd-34920.firebaseio.com/"
        val syllabusRef = Firebase.database(databaseUrl).reference.child("syllabus")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val yearInfoList = mutableListOf<YearInfo>()
                    val yearMap = mutableMapOf<Int, Int>()

                    for (yearChild in snapshot.children) {
                        val yearKeyStr = yearChild.key ?: continue
                        val yearNum = when (yearKeyStr) {
                            "1" -> 1
                            "2" -> 2
                            "3" -> 3
                            "4" -> 4
                            "5" -> 5
                            "6" -> 6
                            "first_year" -> 1
                            "second_year" -> 2
                            "third_year" -> 3
                            "fourth_year" -> 4
                            "fifth_year" -> 5
                            "sixth_year" -> 6
                            else -> yearKeyStr.toIntOrNull() ?: continue
                        }

                        val count = if (yearChild.hasChild("subjects")) {
                            yearChild.child("subjects").childrenCount.toInt()
                        } else {
                            yearChild.childrenCount.toInt()
                        }

                        if (count > 0 && yearNum in 1..6) {
                            yearMap[yearNum] = count
                        }
                    }

                    if (yearMap.isEmpty()) {
                        viewModelScope.launch(Dispatchers.IO) {
                            val fallback = loadYearInfoFromAsset(getApplication())
                            trySend(Result.success(fallback))
                        }
                        return
                    }

                    for (y in 1..6) {
                        val cnt = yearMap[y] ?: 0
                        if (cnt > 0) {
                            yearInfoList.add(YearInfo(year = y, subjectCount = cnt))
                        }
                    }

                    trySend(Result.success(yearInfoList.sortedBy { it.year }))
                } catch (_: Exception) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val fallback = loadYearInfoFromAsset(getApplication())
                        trySend(Result.success(fallback))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch(Dispatchers.IO) {
                    val fallback = loadYearInfoFromAsset(getApplication())
                    trySend(Result.success(fallback))
                }
            }
        }

        syllabusRef.addListenerForSingleValueEvent(listener)
        awaitClose { syllabusRef.removeEventListener(listener) }
    }

    private suspend fun loadYearInfoFromAsset(context: Context): List<YearInfo> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("syllabus_default.json").bufferedReader().use { it.readText() }
            val rootObj = JSONObject(jsonString)
            val targetObj = if (rootObj.has("syllabus")) rootObj.getJSONObject("syllabus") else rootObj

            val list = mutableListOf<YearInfo>()
            for (y in 1..6) {
                val yKey = y.toString()
                if (targetObj.has(yKey) && targetObj.getJSONObject(yKey).has("subjects")) {
                    val count = targetObj.getJSONObject(yKey).getJSONArray("subjects").length()
                    if (count > 0) {
                        list.add(YearInfo(year = y, subjectCount = count))
                    }
                }
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }
}
