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

    private val _membersList = MutableLiveData<List<Members>>(listOf())
    val membersList: LiveData<List<Members>>
        get() = _membersList

    val hasAnyGroupsInGroupList = MutableLiveData(false)

    private fun getGroup(groupId: Int) = groupList.value!!.getGroupFrom(groupId)

    private fun getGroup(memberPrimaryKey: String) = groupList.value!!.getGroupFrom(memberPrimaryKey)

    private fun getGroupMembersList(groupId: Int): ArrayList<Members> {
        val group = getGroup(groupId)
        return group.members
    }

    fun insertGroup(groupName: String) {
        val group = Group(name = groupName)
        viewModelScope.launch {
            groupRepository.insertGroup(group)
        }
    }

    fun insertMemberIntoGroup(groupId: Int, memberId: String, memberName: String, checked: Boolean) {
        val insertedGroup = getGroup(groupId)
        val membersList = getGroupMembersList(groupId)
        val primaryKeyIdForMembers = getIncrementedPrimaryKeyIdForMembers(insertedGroup)

        val memberPrimaryKey = getMemberPrimaryKey(groupId, primaryKeyIdForMembers)

        var autoNumberingMemberId = insertedGroup.autoNumberingMemberId
        val preciseMemberId: Int =
            if (memberId.isEmpty()) {
                autoNumberingMemberId++
                autoNumberingMemberId
            } else {
                memberId.toInt()
            }

        membersList.add(
            Members(memberPrimaryKey, preciseMemberId, memberName, checked)
        )

        val updatedGroup = insertedGroup.copy(
            members = membersList,
            autoNumberingMemberId = autoNumberingMemberId,
            primaryKeyIdForMembers = primaryKeyIdForMembers
        )

        viewModelScope.launch {
            groupRepository.updateGroup(updatedGroup)
        }
    }

    private fun getMemberPrimaryKey(groupId: Int, primaryKeyIdForMembers: Int): String {
        return "${groupId}_${primaryKeyIdForMembers}"
    }


    private fun getIncrementedPrimaryKeyIdForMembers(group: Group): Int {
        var primaryKeyIdForMembers = group.primaryKeyIdForMembers
        primaryKeyIdForMembers++

        return primaryKeyIdForMembers
    }

    fun updateGroupName(groupId: Int, groupName: String) {
        val group = getGroup(groupId)
        val updatedGroup = group.copy(name = groupName)

        viewModelScope.launch {
            groupRepository.updateGroup(updatedGroup)
        }
    }

    fun updateMember(originalMember: Members, updatedMember: Members) {
        val group = getGroup(originalMember.memberPrimaryKey)
        /* 同じオブジェクトをListAdapter.DiffUtilに渡すとリストの更新処理が走らないため、
           型変換を行い、別オブジェクトとしている。*/
        val members = group.members.toMutableList()
        val position = members.indexOf(originalMember)

        members[position] = updatedMember
        val updatedGroup = group.copy(members = members as ArrayList<Members>)

        viewModelScope.launch {
            groupRepository.updateGroup(updatedGroup)
        }
        _membersList.value = members
    }

    fun deleteGroup(groupId: Int) {
        val group = getGroup(groupId)

        viewModelScope.launch {
            groupRepository.deleteGroup(group)
        }
    }

    fun setMembersListBy(groupId: Int) {
        val membersList = getGroupMembersList(groupId)
        _membersList.value = membersList
    }

    fun resetMembersList() {
        _membersList.value = listOf()
    }


}