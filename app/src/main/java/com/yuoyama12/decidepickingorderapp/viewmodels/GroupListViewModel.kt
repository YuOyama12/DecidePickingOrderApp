package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupListViewModel : ViewModel() {

    private var _selectedPosition = MutableLiveData(-1)
    val selectedPosition: LiveData<Int>
        get() = _selectedPosition

    private var _longClickedGroupId = -1
    val longClickedGroupId: Int
        get() = _longClickedGroupId

    private var _longClickedGroupName = ""
    val longClickedGroupName: String
        get() = _longClickedGroupName


    fun setSelectedPosition(position: Int) {
        _selectedPosition.value = position
    }

    fun setLongClickedGroupId(id: Int) {
        _longClickedGroupId = id
    }

    fun setLongClickedGroupName(name: String) {
        _longClickedGroupName = name
    }

    fun resetSelectedPosition() {
        _selectedPosition.value = -1
    }

}