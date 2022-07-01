package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.*
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupRepository
import com.yuoyama12.decidepickingorderapp.data.Member
import com.yuoyama12.decidepickingorderapp.data.getGroupFrom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val groupList = groupRepository.getAll().asLiveData()

    private val _memberList = MutableLiveData<List<Member>>(listOf())
    val memberList: LiveData<List<Member>>
        get() = _memberList

    val hasAnyGroupsInGroupList = MutableLiveData(false)

    private fun getGroup(groupId: Int) = groupList.value!!.getGroupFrom(groupId)

    private fun getGroup(memberPrimaryKey: String) = groupList.value!!.getGroupFrom(memberPrimaryKey)

    private fun getGroupMemberList(groupId: Int): ArrayList<Member> {
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
        val memberList = getGroupMemberList(groupId)
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

        memberList.add(
            Member(memberPrimaryKey, preciseMemberId, memberName, checked)
        )

        val updatedGroup = insertedGroup.copy(
            members = memberList,
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

    fun updateMember(originalMember: Member, updatedMember: Member) {
        val group = getGroup(originalMember.memberPrimaryKey)
        /* 同じオブジェクトをListAdapter.DiffUtilに渡すとリストの更新処理が走らないため、
           型変換を行い、別オブジェクトとしている。*/
        val members = group.members.toMutableList()
        val position = members.indexOf(originalMember)

        members[position] = updatedMember
        val updatedGroup = group.copy(members = members as ArrayList<Member>)

        viewModelScope.launch {
            groupRepository.updateGroup(updatedGroup)
        }
        _memberList.value = members
    }

    fun deleteGroup(groupId: Int) {
        val group = getGroup(groupId)

        viewModelScope.launch {
            groupRepository.deleteGroup(group)
        }
    }

    fun deleteMember(memberPrimaryKey: String, member: Member) {
        val group = getGroup(memberPrimaryKey)
        val members =group.members
        members.remove(member)
        val updatedGroup = group.copy(members = members)

        viewModelScope.launch {
            groupRepository.updateGroup(updatedGroup)
        }
        _memberList.value = members
    }

    fun setMemberListBy(groupId: Int) {
        val memberList = getGroupMemberList(groupId)
        _memberList.value = memberList
    }

    fun resetMemberList() {
        _memberList.value = listOf()
    }


}