package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.*
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

    val groupList = groupRepository.getAll().asLiveData()

    private var _membersList = MutableLiveData<List<Members>>(listOf())
    val membersList: MutableLiveData<List<Members>>
        get() = _membersList

    private fun getGroup(groupId: Int) = groupList.value!!.getGroupFrom(groupId)

    private fun getGroupMembersList(groupId: Int): ArrayList<Members> {
        val group = getGroup(groupId)
        return group.members
    }

    fun insertGroup(listName: String) {
        val group = Group(name = listName)
        viewModelScope.launch {
            groupRepository.insertGroup(group)
        }
    }

    fun insertMemberIntoGroup(groupId: Int, memberId: String, memberName: String, checked: Boolean) {
        val group = getGroup(groupId)
        val membersList = getGroupMembersList(groupId)
        var autoNumberingMemberId = group.autoNumberingMemberId

        val preciseMemberId: Int =
            if (memberId.isEmpty()) {
                autoNumberingMemberId++
                autoNumberingMemberId
            } else {
                memberId.toInt()
            }

        membersList.add(
            Members(preciseMemberId, memberName, checked)
        )

        val updatedGroup = group.copy(members = membersList, autoNumberingMemberId = autoNumberingMemberId)

        viewModelScope.launch {
            groupRepository.insertMemberIntoGroup(updatedGroup)
        }
    }

    fun setMembersListBy(groupId: Int) {
        val membersList = getGroupMembersList(groupId)
        _membersList.value = membersList
    }

}