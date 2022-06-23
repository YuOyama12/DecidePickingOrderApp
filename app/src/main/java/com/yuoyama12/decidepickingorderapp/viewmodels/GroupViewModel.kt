package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : ViewModel() {

    val groupList: LiveData<List<Group>> =
        groupRepository.getAll().asLiveData()

    fun insert(listName: String) {
        val group = Group(name = listName)
        viewModelScope.launch {
            groupRepository.insertGroup(group)
        }
    }
}