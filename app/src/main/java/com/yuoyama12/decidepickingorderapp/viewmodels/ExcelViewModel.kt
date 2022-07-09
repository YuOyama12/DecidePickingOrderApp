package com.yuoyama12.decidepickingorderapp.viewmodels

import androidx.lifecycle.ViewModel
import org.apache.poi.ss.usermodel.Workbook

class ExcelViewModel : ViewModel() {

    private var _workbook: Workbook? = null
    val workbook: Workbook
      get() = _workbook!!

    private var _sheetName = ""
    val sheetName: String   get() = _sheetName

    fun setWorkbook(selectedWorkbook: Workbook) {
        _workbook = selectedWorkbook
    }

    fun setSheetName(sheetName: String) {
        _sheetName = sheetName
    }


}