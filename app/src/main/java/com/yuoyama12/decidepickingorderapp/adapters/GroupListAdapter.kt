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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemGroupBinding.inflate(layoutInflater, parent,false)

        return GroupViewHolder(binding)
    }

    override fun onCurrentListChanged(
        previousList: MutableList<Group>,
        currentList: MutableList<Group>
    ) {
        super.onCurrentListChanged(previousList, currentList)
        if (previousList.size != 0 &&
            (previousList.size != currentList.size ||
                    isOrderChanged(previousList, currentList))
        ) {
            currentList.forEachIndexed { index, group ->
                if (group.groupId == groupListViewModel.selectedGroupId) {
                    groupListViewModel.setSelectedStateInfo(index, group.groupId)
                    return
                }
            }
        }
    }

    private fun isOrderChanged(previousList: MutableList<Group>, currentList: MutableList<Group>): Boolean {
        if (previousList == currentList) return false

        previousList.forEachIndexed { index, group ->
            if (group.groupId != currentList[index].groupId) {
                return true
            }
        }
        return false
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Group>(){
            override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem.groupId == newItem.groupId
            }
            override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class GroupViewHolder(
        private val binding: ListItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(group: Group) {
            changeSelectionState(adapterPosition)

            binding.listItemGroupName.text = group.name

            itemView.apply {
                setOnLongClickListener {
                    onItemClicked(group)
                    setSelectedStateIfPossible(adapterPosition, group.groupId)

                    groupListViewModel.setLongClickedGroupId(group.groupId)
                    groupListViewModel.setLongClickedGroupName(group.name)
                    false
                }

                setOnClickListener {
                    onItemClicked(group)
                    setSelectedStateIfPossible(adapterPosition, group.groupId)
                }
            }

            binding.addMemberButton.setOnClickListener {
                setSelectedStateIfPossible(adapterPosition, group.groupId)
                buttonClickedAction(group)
            }
        }

        private fun changeSelectionState(position: Int) {
            when (groupListViewModel.selectedPosition) {
                -1 ->  itemView.isSelected = false
                position ->  itemView.isSelected = true
                else -> itemView.isSelected = false
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun setSelectedStateIfPossible(position: Int, groupId: Int) {
            if (groupListViewModel.selectedPosition != position) {
                notifyDataSetChanged()
                groupListViewModel.setSelectedStateInfo(position, groupId)
            }
        }

    }

}

