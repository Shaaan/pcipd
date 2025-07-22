package `in`.shaaan.pcipharmd

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import `in`.shaaan.pcipharmd.databinding.ListItemAdBinding
import `in`.shaaan.pcipharmd.databinding.ListItemSubjectBinding

private const val ITEM_VIEW_TYPE_SUBJECT = 0
private const val ITEM_VIEW_TYPE_AD = 1

class SyllabusAdapter(private val onSubjectClick: (endpoint: String) -> Unit) :
    ListAdapter<ListItem, RecyclerView.ViewHolder>(SyllabusDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ListItem.SubjectItem -> ITEM_VIEW_TYPE_SUBJECT
            is ListItem.AdItem -> ITEM_VIEW_TYPE_AD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_VIEW_TYPE_SUBJECT -> {
                val binding = ListItemSubjectBinding.inflate(inflater, parent, false)
                SubjectViewHolder(binding)
            }
            ITEM_VIEW_TYPE_AD -> {
                val binding = ListItemAdBinding.inflate(inflater, parent, false)
                AdViewHolder(binding)
            }
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SubjectViewHolder -> {
                val subjectItem = getItem(position) as ListItem.SubjectItem
                holder.bind(subjectItem, onSubjectClick)
            }
            is AdViewHolder -> {
                val adItem = getItem(position) as ListItem.AdItem
                holder.bind(adItem)
            }
        }
    }

    class SubjectViewHolder(private val binding: ListItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListItem.SubjectItem, onSubjectClick: (endpoint: String) -> Unit) {
            binding.subjectButton.setText(item.textResId)
            binding.subjectButton.setOnClickListener {
                onSubjectClick(item.endpoint)
            }
        }
    }

    class AdViewHolder(private val binding: ListItemAdBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListItem.AdItem) {
            // The AdUtil will handle populating the ad and setting visibility
            AdUtil.refreshAd(item.activity, binding.adPlaceholder, binding.adCardView)
        }
    }
}

class SyllabusDiffCallback : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return (oldItem is ListItem.SubjectItem && newItem is ListItem.SubjectItem && oldItem.endpoint == newItem.endpoint) ||
                (oldItem is ListItem.AdItem && newItem is ListItem.AdItem && oldItem.activity == newItem.activity)
    }

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }
}