package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.ViewModel
import org.apache.poi.ss.usermodel.Workbook

class ExcelViewModel : ViewModel() {

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
    val contentListForId: ArrayList<String>
        get() = _contentListForId

    private var _contentListForName = arrayListOf<String>()
    val contentListForName: ArrayList<String>
        get() = _contentListForName

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

    fun resetAll() {
        _workbook = null
        _sheetName = ""
        _columnAsId = ""
        _columnAsName = ""
        _contentListForId = arrayListOf()
        _contentListForName = arrayListOf()
    }


}