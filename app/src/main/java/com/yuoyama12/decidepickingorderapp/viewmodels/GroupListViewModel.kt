package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GroupListViewModel : ViewModel() {

    private var _selectedPosition = MutableLiveData(-1)
    val selectedPosition: MutableLiveData<Int>
        get() = _selectedPosition

    fun setSelectedPosition(position: Int) {
        _selectedPosition.value = position
    }

}