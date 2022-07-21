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

    val colorsListForColorMarker = MutableLiveData<ArrayList<Int>>()

    val ascendingOrderCheckState = MutableLiveData(false)

    private var _restoredAscendingOrderCheckState = false
    val restoredAscendingOrderCheckState: Boolean
        get() = _restoredAscendingOrderCheckState

    private var _restoredColorList = arrayListOf<Int>()
    val restoredColorList: ArrayList<Int>
        get() = _restoredColorList

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

    fun createColorsListForColorMarker(colorList: ArrayList<Int>) {
        val colorsList = getColorsListForColorMarker(colorList)
        colorsListForColorMarker.value = colorsList
    }

    private fun getColorsListForColorMarker(colorList: ArrayList<Int>): ArrayList<Int> {
        val memberList = memberList.value!!
        val colorsListForColorMarker = arrayListOf<Int>()
        var previousColor = 0

        for (member in memberList){
            var color = colorList.random()
            while (color == previousColor
                && hasNotAllTheSameColors(colorList)){
                color = colorList.random()
            }

            colorsListForColorMarker.add(color)

            previousColor = color
        }

        return colorsListForColorMarker
    }

    private fun hasNotAllTheSameColors(colorList: ArrayList<Int>): Boolean {
        val firstColor = colorList[0]
        for(color in colorList){
            if (color != firstColor){
                return true
            }
        }
        return false
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

    fun restoreColorList(colorList: ArrayList<Int>) {
        _restoredColorList = colorList
    }

    fun resetCurrentItemPosition() {
        currentItemPosition.value = 0
    }

    fun resetColorList() {
        _restoredColorList = arrayListOf()
    }

}
