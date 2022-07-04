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

    val memberList = MutableLiveData<MutableList<Member>>()

    val currentItemPosition = MutableLiveData(0)

    val ascendingOrderCheckState = MutableLiveData(false)

    private var _restoredAscendingOrderCheckState = false
    val restoredAscendingOrderCheckState: Boolean
        get() = _restoredAscendingOrderCheckState

    private var _selectedGroup: Group? = null
    val selectedGroup: Group?
        get() = _selectedGroup

    fun setSelectedGroup(group: Group) {
        _selectedGroup = group
    }

    fun createMemberList() {
        var createdMemberList: ArrayList<Member>?

        viewModelScope.launch {
            createdMemberList = groupRepository.getMembersFrom(_selectedGroup!!.groupId)

            memberList.value =
                if (ascendingOrderCheckState.value == true) {
                    createdMemberList!!.sortedBy { it.memberId }.toMutableList()
                } else {
                    createdMemberList!!.shuffled().toMutableList()
                }
        }
        restoreAscendingOrderCheckState()
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

    private fun restoreAscendingOrderCheckState() {
        _restoredAscendingOrderCheckState = ascendingOrderCheckState.value!!
    }

    fun resetCurrentItemPosition() {
        currentItemPosition.value = 0
    }


}
