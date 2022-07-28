package com.yuoyama12.decidepickingorderapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroupRepository @Inject constructor(private val groupDao: GroupDao) {
    fun getGroups(): Flow<List<Group>> = groupDao.getGroups()

    fun getGroupsSortedByName(): Flow<List<Group>> =
        groupDao.getGroups().map { groups ->
            groups.sortedBy { it.name }
        }

    fun getGroupsSortedByCreationTimeLatestFirst(): Flow<List<Group>> =
        groupDao.getGroups().map { group ->
            group.sortedBy { it.groupId }.reversed()
        }

    suspend fun getMembersFrom(groupId: Int): MemberList = groupDao.getMembersFrom(groupId)

    suspend fun insertGroup(group: Group) {
        groupDao.insertGroup(group)
    }

    suspend fun insertGroupAndReturnId(group: Group): Long {
        return groupDao.insertGroupAndReturnId(group)
    }

    suspend fun updateGroup(group: Group) {
        groupDao.updateGroup(group)
    }

    suspend fun deleteGroup(group: Group) {
        groupDao.deleteGroup(group)
    }

}