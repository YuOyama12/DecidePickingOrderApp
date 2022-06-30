package com.yuoyama12.decidepickingorderapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.databinding.ListItemGroupBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupListViewModel

class GroupListAdapter(
    val groupListViewModel: GroupListViewModel,
    val viewLifecycleOwner: LifecycleOwner,
    val onItemClicked: (Group) -> Unit,
    val buttonClickedAction: (Group) -> Unit
) : ListAdapter<Group, GroupListAdapter.GroupViewHolder>(diffCallback) {

    private var selectedPosition: Int =
        groupListViewModel.selectedPosition.value!!


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

        init {
            groupListViewModel.selectedPosition.observe(viewLifecycleOwner) {
                selectedPosition = it
            }
        }

        fun bind(group: Group) {
            changeSelectionState(adapterPosition)

            binding.listItemGroupName.text = group.name

            itemView.apply {
                setOnLongClickListener {
                    onItemClicked(group)
                    setSelectedPositionIfPossible(adapterPosition)

                    groupListViewModel.setLongClickedGroupId(group.groupId)
                    groupListViewModel.setLongClickedGroupName(group.name)
                    false
                }

                setOnClickListener {
                    onItemClicked(group)
                    setSelectedPositionIfPossible(adapterPosition)
                }
            }

            binding.addMemberButton.setOnClickListener {
                setSelectedPositionIfPossible(adapterPosition)
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
            }
        }

    }

}

