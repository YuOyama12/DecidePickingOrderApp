package com.yuoyama12.decidepickingorderapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.NullPointerException

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    val groupId: Int = 0,
    val name: String,
    val members: ArrayList<Member> = arrayListOf(),
    val autoNumberingMemberId: Int = 0,
    val primaryKeyIdForMembers: Int = 0
)

fun List<Group>.getGroupFrom(groupId: Int): Group {
    var returnedGroup: Group? = null
    for (group in this) {
        if (group.groupId == groupId) {
            returnedGroup = group
            break
        }
    }
    return returnedGroup ?: throw NullPointerException("Can't find any group from the id!")
}

fun List<Group>.getGroupFrom(memberPrimaryKey: String): Group {
    var returnedGroup: Group? = null
    for (group in this) {
        for (member in group.members) {
            if (member.memberPrimaryKey == memberPrimaryKey) {
                returnedGroup = group
                break
            }
        }
    }
    return returnedGroup ?: throw NullPointerException("Can't find any group from the member's primary key!")
}