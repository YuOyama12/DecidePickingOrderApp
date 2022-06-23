package com.yuoyama12.decidepickingorderapp.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface GroupDao {
    @Insert
    suspend fun insertGroup(group: Group)
}