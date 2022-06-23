package com.yuoyama12.decidepickingorderapp.data

data class Members(
    val memberId: Int,
    val name: String,
    val isChecked: Boolean
)

class MembersList: ArrayList<Members>()
