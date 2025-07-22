package `in`.shaaan.pcipharmd

import android.app.Activity

sealed class ListItem {
    data class SubjectItem(val textResId: Int, val endpoint: String) : ListItem()
    data class AdItem(val activity: Activity) : ListItem()
}