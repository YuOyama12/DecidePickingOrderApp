package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.ViewModel

class MembersListViewModel : ViewModel() {

    private var _longClickedMemberPrimaryKey = ""
    val longClickedMemberPrimaryKey: String
        get() = _longClickedMemberPrimaryKey

    private var _longClickedMemberId = -1
    val longClickedMemberId: Int
        get() = _longClickedMemberId

    private var _longClickedMemberName = ""
    val longClickedMemberName: String
        get() = _longClickedMemberName

    private var _longClickedMemberCheckBox = false
    val longClickedMemberCheckBox: Boolean
        get() = _longClickedMemberCheckBox

    fun setLongClickedPrimaryKeyId(memberPrimaryKey: String) {
        _longClickedMemberPrimaryKey = memberPrimaryKey
    }

    fun setLongClickedMemberId(memberId: Int) {
        _longClickedMemberId = memberId
    }

    fun setLongClickedMemberName(memberName: String) {
        _longClickedMemberName = memberName
    }

    fun setLongClickedMemberCheckBox(isChecked: Boolean) {
        _longClickedMemberCheckBox = isChecked
    }




}