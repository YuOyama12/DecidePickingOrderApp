package com.yuoyama12.decidepickingorderapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class TypeConverter {
    @TypeConverter
    fun convertMemberListToString(memberList: ArrayList<Member>): String?{
        return Gson().toJson(memberList)
    }

    @TypeConverter
    fun convertStringToMemberList(value: String?): MemberList?{
        return if (value == null) null
        else Gson().fromJson(value, MemberList::class.java)
    }
}