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

    val currentItemPosition = MutableLiveData(0)

    private var _selectedGroup: Group? = null
    val selectedGroup: Group?
        get() = _selectedGroup

    fun setSelectedGroup(group: Group) {
        _selectedGroup = group
    }

    fun createMemberList() {
        viewModelScope.launch {
            memberList.value = groupRepository.getMembersFrom(_selectedGroup!!.groupId)
        }
    }

    fun goNextItem() {
        val lastIndex = memberList.value!!.lastIndex
        when (currentItemPosition.value) {
            lastIndex -> changeCurrentItemPosition(0)
            else -> changeCurrentItemPosition(
                currentItemPosition.value!!.plus(1)
            )
        }
    }

    fun goPreviousItem() {
        val lastIndex = memberList.value!!.lastIndex
        when (currentItemPosition.value) {
            0 -> changeCurrentItemPosition(lastIndex)
            else -> changeCurrentItemPosition(
                currentItemPosition.value!!.minus(1)
            )
        }
    }

    private fun changeCurrentItemPosition(position: Int) {
        currentItemPosition.value = position
    }

    fun resetCurrentItemPosition() {
        currentItemPosition.value = 0
    }
}
