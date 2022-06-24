package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupRepository
import com.yuoyama12.decidepickingorderapp.data.Members
import com.yuoyama12.decidepickingorderapp.data.getGroupFrom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val groupList: LiveData<List<Group>> =
        groupRepository.getAll().asLiveData()

    fun insertGroup(listName: String) {
        val group = Group(name = listName)
        viewModelScope.launch {
            groupRepository.insertGroup(group)
        }
    }

    fun insertMemberIntoGroup(groupId: Int, memberId: String, memberName: String, checked: Boolean) {
        val group = groupList.value!!.getGroupFrom(groupId)
        val membersList = group.members
        var autoNumberingMemberId = group.autoNumberingMemberId

        val preciseMemberId: Int =
            if (memberId.isEmpty()) {
                ++autoNumberingMemberId
                autoNumberingMemberId
            } else {
                memberId.toInt()
            }

        membersList.add(
            Members(preciseMemberId, memberName, checked)
        )

        viewModelScope.launch {
            groupRepository.insertMemberIntoGroup(
                Group(group.groupId, group.name, membersList,autoNumberingMemberId)
            )
        }
    }
}