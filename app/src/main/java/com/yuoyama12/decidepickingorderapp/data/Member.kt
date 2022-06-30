package com.yuoyama12.decidepickingorderapp.data


data class Member(
    val memberPrimaryKey: String,
    val memberId: Int,
    val name: String,
    val isChecked: Boolean
)

class MemberList: ArrayList<Member>()
