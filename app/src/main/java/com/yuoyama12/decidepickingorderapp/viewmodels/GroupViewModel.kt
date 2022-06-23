package com.yuoyama12.decidepickingorderapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupDatabase
import kotlinx.coroutines.launch

class GroupViewModel(application: Application) : AndroidViewModel(application) {
    private val groupDao = GroupDatabase.getInstance(application).groupDao()

    fun insert(listName: String) {
        val group = Group(name = listName)
        viewModelScope.launch {
            groupDao.insertGroup(group)
        }
    }
}