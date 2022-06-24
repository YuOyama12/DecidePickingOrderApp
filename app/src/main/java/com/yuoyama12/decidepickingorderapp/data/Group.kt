package com.yuoyama12.decidepickingorderapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    val groupId: Int = 0,
    val name: String,
    val members: ArrayList<Members> = arrayListOf()
)
