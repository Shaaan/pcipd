package `in`.shaaan.pcipharmd

sealed class ListItem {
    data class SubjectItem(val textResId: Int, val endpoint: String) : ListItem()
    data object AdItem : ListItem()
}