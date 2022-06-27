package com.yuoyama12.decidepickingorderapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.databinding.ListItemGroupBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupListViewModel

class GroupListAdapter(
    val groupListViewModel: GroupListViewModel,
    val onItemClicked: (Group) -> Unit,
    val buttonClickedAction: (Group) -> Unit
) : ListAdapter<Group, GroupListAdapter.GroupViewHolder>(diffCallback) {

    private var selectedPosition: Int =
        groupListViewModel.selectedPosition.value ?: -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemGroupBinding.inflate(layoutInflater, parent,false)

        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = getItem(position)
        val adapterPosition = holder.adapterPosition

        holder.bind(item, adapterPosition)
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

        fun bind(group: Group, adapterPosition: Int) {
            changeSelectionState(adapterPosition)

            binding.listItemGroupName.text = group.name

            itemView.setOnClickListener {
                onItemClicked(group)
                setSelectedPositionIfPossible(adapterPosition)
            }

            binding.addMemberButton.setOnClickListener {
                buttonClickedAction(group)
            }
        }

        private fun changeSelectionState(adapterPosition: Int) {
            when (selectedPosition) {
                -1 ->  itemView.isSelected = false
                adapterPosition ->  itemView.isSelected = true
                else -> itemView.isSelected = false
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun setSelectedPositionIfPossible(adapterPosition: Int) {
            if (selectedPosition != adapterPosition) {
                notifyDataSetChanged()
                groupListViewModel.setSelectedPosition(adapterPosition)

                selectedPosition = groupListViewModel.selectedPosition.value!!
            }
        }

    }

}

