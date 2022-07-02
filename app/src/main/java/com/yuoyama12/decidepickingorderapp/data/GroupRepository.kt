package com.yuoyama12.decidepickingorderapp.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(private val groupDao: GroupDao) {
    fun getAll(): Flow<List<Group>> = groupDao.getAll()

    suspend fun getMembersFrom(groupId: Int): MemberList = groupDao.getMembersFrom(groupId)

    suspend fun insertGroup(group: Group) {
        groupDao.insertGroup(group)
    }

    suspend fun updateGroup(group: Group) {
        groupDao.updateGroup(group)
    }

    suspend fun deleteGroup(group: Group) {
        groupDao.deleteGroup(group)
    }

}