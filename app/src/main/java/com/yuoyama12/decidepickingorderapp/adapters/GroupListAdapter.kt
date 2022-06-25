package com.yuoyama12.decidepickingorderapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.databinding.ListItemGroupBinding

class GroupListAdapter(
    val onItemClicked: (Group) -> Unit,
    val buttonClickedAction: (Group) -> Unit
) : ListAdapter<Group, GroupListAdapter.GroupViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemGroupBinding.inflate(layoutInflater, parent,false)

        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Group>(){
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.groupId == newItem.groupId
            }
        }
    }

    inner class GroupViewHolder(
        private val binding: ListItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            binding.listItemGroupName.text = group.name

            itemView.setOnClickListener { onItemClicked(group) }

            binding.addMemberButton.setOnClickListener {
                buttonClickedAction(group)
            }
        }
    }

}

