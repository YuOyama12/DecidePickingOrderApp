package com.yuoyama12.decidepickingorderapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yuoyama12.decidepickingorderapp.data.Members
import com.yuoyama12.decidepickingorderapp.databinding.ListItemMemberBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.MembersListViewModel

class MembersListAdapter(
    private val memberListViewModel: MembersListViewModel
) : ListAdapter<Members, MembersListAdapter.MembersViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemMemberBinding.inflate(layoutInflater, parent,false)

        return MembersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MembersViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    
    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Members>(){
            override fun areItemsTheSame(oldItem: Members, newItem: Members): Boolean {
                return oldItem.memberPrimaryKey == newItem.memberPrimaryKey
            }
            override fun areContentsTheSame(oldItem: Members, newItem: Members): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MembersViewHolder(
        private val binding: ListItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(members: Members) {
            binding.apply {
                listItemMemberId.text = members.memberId.toString()
                listItemMemberName.text = members.name
                listItemMemberCheckBox.isChecked = members.isChecked
            }

            itemView.setOnLongClickListener {
                memberListViewModel.apply {
                    this.setLongClickedPrimaryKeyId(members.memberPrimaryKey)
                    this.setLongClickedMemberId(members.memberId)
                    this.setLongClickedMemberName(members.name)
                    this.setLongClickedMemberCheckBox(members.isChecked)
                }
                false
            }

        }
    }

}

