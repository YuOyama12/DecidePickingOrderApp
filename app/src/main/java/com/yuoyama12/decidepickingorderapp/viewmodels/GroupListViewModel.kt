package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.ViewModel

class GroupListViewModel : ViewModel() {

    private var _selectedPosition = -1
    val selectedPosition: Int
        get() = _selectedPosition

    private var _longClickedGroupId = -1
    val longClickedGroupId: Int
        get() = _longClickedGroupId

    private var _longClickedGroupName = ""
    val longClickedGroupName: String
        get() = _longClickedGroupName


    fun setSelectedPosition(position: Int) {
        _selectedPosition = position
    }

    fun setLongClickedGroupId(id: Int) {
        _longClickedGroupId = id
    }

    fun setLongClickedGroupName(name: String) {
        _longClickedGroupName = name
    }

}