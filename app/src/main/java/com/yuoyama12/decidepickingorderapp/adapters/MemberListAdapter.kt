package com.yuoyama12.decidepickingorderapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yuoyama12.decidepickingorderapp.data.Member
import com.yuoyama12.decidepickingorderapp.databinding.ListItemMemberBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.MemberListViewModel

class MemberListAdapter(
    private val memberListViewModel: MemberListViewModel
) : ListAdapter<Member, MemberListAdapter.MemberViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemMemberBinding.inflate(layoutInflater, parent,false)

        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
    
    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<Member>(){
            override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem.memberPrimaryKey == newItem.memberPrimaryKey
            }
            override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MemberViewHolder(
        private val binding: ListItemMemberBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(member: Member) {
            binding.apply {
                listItemMemberId.text = member.memberId.toString()
                listItemMemberName.text = member.name
                listItemMemberCheckBox.isChecked = member.isChecked
            }

            itemView.setOnLongClickListener {
                memberListViewModel.apply {
                    this.setLongClickedMemberPrimaryKey(member.memberPrimaryKey)
                    this.setLongClickedMemberId(member.memberId)
                    this.setLongClickedMemberName(member.name)
                    this.setLongClickedMemberCheckBox(member.isChecked)
                }
                false
            }

        }
    }

}

