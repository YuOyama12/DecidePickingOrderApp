package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.LiveData
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

    private var _currentItemPosition = MutableLiveData(0)
    val currentItemPosition: LiveData<Int>
        get() = _currentItemPosition

    private var selectedGroup: Group? = null

    fun setSelectedGroup(group: Group) {
        selectedGroup = group
    }

    fun createMemberList() {
        viewModelScope.launch {
            memberList.value = groupRepository.getMembersFrom(selectedGroup!!.groupId)
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
        _currentItemPosition.value = position
    }
}
