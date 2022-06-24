package com.yuoyama12.decidepickingorderapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM `Group`")
    fun getAll(): Flow<List<Group>>

    @Insert
    suspend fun insertGroup(group: Group)

    @Update
    suspend fun insertMemberIntoGroup(group: Group)
}