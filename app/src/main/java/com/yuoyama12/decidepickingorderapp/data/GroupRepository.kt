package com.yuoyama12.decidepickingorderapp.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(private val groupDao: GroupDao) {
    fun getAll(): Flow<List<Group>> = groupDao.getAll()

    suspend fun insertGroup(group: Group) {
        groupDao.insertGroup(group)
    }

    suspend fun insertMemberIntoGroup(group: Group) {
        groupDao.insertMemberIntoGroup(group)
    }
}