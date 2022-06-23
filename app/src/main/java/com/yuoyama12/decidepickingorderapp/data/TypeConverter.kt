package com.yuoyama12.decidepickingorderapp.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class TypeConverter {
    @TypeConverter
    fun convertMembersListToString(membersList: ArrayList<Members>): String?{
        return Gson().toJson(membersList)
    }

    @TypeConverter
    fun convertStringToMembersList(value: String?): ArrayList<Members>?{
        return if (value == null) null
        else Gson().fromJson(value, MembersList::class.java)
    }
}