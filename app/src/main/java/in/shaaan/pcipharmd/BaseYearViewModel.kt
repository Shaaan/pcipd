package `in`.shaaan.pcipharmd

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel

class BaseYearViewModel(application: Application) : AndroidViewModel(application) {

    init {
        AdUtil.loadInterAd(application)
    }

    fun onSubjectClicked(activity: Activity, endpoint: String) {
        val url = "${SyllabusRepository.BASE_URL}$endpoint"
        AdUtil.showInterAd(activity, url)
    }
}