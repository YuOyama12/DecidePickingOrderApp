package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupRepository
import com.yuoyama12.decidepickingorderapp.data.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val memberList = MutableLiveData<ArrayList<Member>>()

    private var selectedGroup: Group? = null

    fun setSelectedGroup(group: Group) {
        selectedGroup = group
    }

    fun createMemberList() {
        viewModelScope.launch {
            memberList.value = groupRepository.getMembersFrom(selectedGroup!!.groupId)
        }
    }
}
