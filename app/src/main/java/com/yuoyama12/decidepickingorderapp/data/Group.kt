package com.yuoyama12.decidepickingorderapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey
    @ColumnInfo(name = "group_id") val groupId: Int? = null,
    val name: String,

    //TODO: TypeConverterの対応。
    //val members: Members
)
