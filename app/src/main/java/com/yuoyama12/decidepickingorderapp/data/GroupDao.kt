package com.yuoyama12.decidepickingorderapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM `Group`")
    fun getGroups(): Flow<List<Group>>

    @Query("SELECT members FROM `Group` WHERE group_id = :groupId")
    suspend fun getMembersFrom(groupId: Int): MemberList

    @Insert
    suspend fun insertGroup(group: Group)

    @Insert
    suspend fun insertGroupAndReturnId(group: Group): Long

    @Update
    suspend fun updateGroup(group: Group)

    @Delete
    suspend fun deleteGroup(group: Group)
}