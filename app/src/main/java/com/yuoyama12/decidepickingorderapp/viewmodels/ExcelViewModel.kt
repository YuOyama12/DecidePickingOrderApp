package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.GroupRepository
import com.yuoyama12.decidepickingorderapp.data.Member
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.Workbook
import javax.inject.Inject

@HiltViewModel
class ExcelViewModel @Inject constructor (
    private val groupRepository: GroupRepository
) : ViewModel() {

    private var _workbook: Workbook? = null
    val workbook: Workbook
      get() = _workbook!!

    private var _sheetName = ""
    val sheetName: String
        get() = _sheetName

    private var _columnAsId = ""
    val columnAsId: String
        get() = _columnAsId

    private var _columnAsName = ""
    val columnAsName: String
        get() = _columnAsName

    private var _contentListForId = arrayListOf<String>()
    private val contentListForId: ArrayList<String>
        get() = _contentListForId

    private var _contentListForName = arrayListOf<String>()
    private val contentListForName: ArrayList<String>
        get() = _contentListForName

    private var autoNumberingMemberId = 1
    private var  primaryKeyIdForMembers = 1
    private var returnedGroupId = -1

    fun setWorkbook(selectedWorkbook: Workbook) {
        _workbook = selectedWorkbook
    }

    fun setSheetName(sheetName: String) {
        _sheetName = sheetName
    }

    fun setColumnAsId(columnName: String) {
        _columnAsId = columnName
    }

    fun setColumnAsName(columnName: String) {
        _columnAsName = columnName
    }

    fun setContentListForId(contentList: ArrayList<String>) {
        _contentListForId = contentList
    }

    fun setContentListForName(contentList: ArrayList<String>) {
        _contentListForName = contentList
    }

    fun createNewGroupWithMembers() {
        viewModelScope.launch {
            insertGroupAndSetGroupId(sheetName)

            val members = getMembersFromExcel()

            val group = Group(
                returnedGroupId,
                sheetName,
                members,
                autoNumberingMemberId,
                primaryKeyIdForMembers
            )

            groupRepository.updateGroup(group)
        }
    }

    private suspend fun insertGroupAndSetGroupId(groupName: String) {
        val group = Group(name = groupName)
        //GroupのIDはInt値で設定しているため変換処理が必要。
        returnedGroupId = groupRepository.insertGroupAndReturnId(group).toInt()
    }

    private fun getMembersFromExcel(): ArrayList<Member> {
        val memberList: ArrayList<Member> = arrayListOf()

        for (position in 0..contentListForName.lastIndex) {
            val memberPrimaryKey = "${returnedGroupId}_${primaryKeyIdForMembers}"
            val memberId = getPreciseMemberId(contentListForId[position])
            val memberName = contentListForName[position]

            primaryKeyIdForMembers++

            val member = Member(
                memberPrimaryKey,
                memberId,
                memberName,
                false
            )
            memberList.add(member)
        }

        return memberList
    }

    private fun getPreciseMemberId(id: String): Int {
        return if (id.isEmpty()) {
            getIncrementedAutoNumberingMemberId()
        } else {
            //Excelセル内に整数値以外が入っていた場合、
            //自動採番されたIDを使用。
            try {
                id.toInt()
            } catch (e: NumberFormatException) {
                getIncrementedAutoNumberingMemberId()
            }
        }
    }

    private fun getIncrementedAutoNumberingMemberId(): Int {
        return autoNumberingMemberId++
    }

    fun resetAll() {
        _workbook = null
        _sheetName = ""
        _columnAsId = ""
        _columnAsName = ""
        _contentListForId = arrayListOf()
        _contentListForName = arrayListOf()
        autoNumberingMemberId = 1
        primaryKeyIdForMembers = 1
        returnedGroupId = -1
    }

}