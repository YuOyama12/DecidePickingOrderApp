package com.yuoyama12.decidepickingorderapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupDatabase
import com.yuoyama12.decidepickingorderapp.data.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    fun insert(listName: String) {
        val group = Group(name = listName)
        viewModelScope.launch {
            groupRepository.insertGroup(group)
        }
    }
}