package com.yuoyama12.decidepickingorderapp.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(private val groupDao: GroupDao) {
    suspend fun insertGroup(group: Group) {
        groupDao.insertGroup(group)
    }
}