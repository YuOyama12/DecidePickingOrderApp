package com.yuoyama12.decidepickingorderapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yuoyama12.decidepickingorderapp.dialogs.SelectExcelInfoDialog
import com.yuoyama12.decidepickingorderapp.viewmodels.ExcelViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.*
import java.lang.IllegalStateException

enum class Excel {
    EXCEL_SHEET,
    EXCEL_ID_COLUMN,
    EXCEL_NAME_COLUMN
}

enum class BundleKey {
    ARG_EXCEL_KEY,
    ARG_SELECTION_KEY
}

class ExcelDataProcessor(
    private val fragment: Fragment,
    private val activity: FragmentActivity,
    private val excelViewModel: ExcelViewModel
) {

    init {
        SelectExcelInfoDialog.prepareFragmentResultListener(
            fragment,
            { decideWhichColumnToUseForIdBy(excelViewModel.workbook) },
            { decideWhichColumnToUseForNameBy(excelViewModel.workbook) },
            {
                if (excelViewModel.columnAsId.isNotEmpty()){
                    val contentListForId = getContentList(Excel.EXCEL_ID_COLUMN)
                    setContentList(Excel.EXCEL_ID_COLUMN, contentListForId)
                }

                val contentListForName = getContentList(Excel.EXCEL_NAME_COLUMN)
                setContentList(Excel.EXCEL_NAME_COLUMN, contentListForName)

                excelViewModel.createNewGroupWithMembers()
            }
        )

    }

    fun execute(result: ActivityResult) = runBlocking {
        excelViewModel.resetAll()

        val selectedWorkbook =
            withContext(Dispatchers.Default) {
                val uri = result.data!!.data
                getWorkbook(uri!!)
            }

        excelViewModel.setWorkbook(selectedWorkbook)

        decideWhichSheetToUseBy(excelViewModel.workbook)
    }

    private fun getWorkbook(uri: Uri): Workbook {
        val inputStream = activity.contentResolver?.openInputStream(uri)
        return WorkbookFactory.create(inputStream)
    }

    private fun decideWhichSheetToUseBy(workbook: Workbook) {
        if (workbook.numberOfSheets > 1){
            val sheetNamesList = getSheetNamesList(workbook)

            createSelectExcelInfoDialogFrom(Excel.EXCEL_SHEET, sheetNamesList)

        }else{
            val sheetName = workbook.getSheetName(0)
            excelViewModel.setSheetName(sheetName)
        }
    }

    private fun getSheetNamesList(workbook: Workbook): ArrayList<String> {
        val sheetNamesList = arrayListOf<String>()
        val numberOfSheets = workbook.numberOfSheets

        for (num in 0 until numberOfSheets) {
            val sheetName = workbook.getSheetName(num).toString()
            sheetNamesList.add(sheetName)
        }
        return sheetNamesList
    }

    private fun decideWhichColumnToUseForIdBy(workbook: Workbook) {
        val sheetName = excelViewModel.sheetName
        if (sheetName != ""){
            val sheet = workbook.getSheet(sheetName)
            val titlesAndPositionMap = getTableTitlesAndPosition(sheet)
            val titlesList = getTitlesList(titlesAndPositionMap)

            titlesList.add(getString(R.string.excel_dialog_not_use_item))

            createSelectExcelInfoDialogFrom(Excel.EXCEL_ID_COLUMN, titlesList)
        }else{
            createErrorDialog(getString(R.string.excel_dialog_no_available_sheet_data))
        }
    }

    private fun getTableTitlesAndPosition(sheet: Sheet): ArrayList<Map<String, Any>> {
        val titleAndPositionMapList = arrayListOf<Map<String, Any>>()
        val firstContentOwnedRow = getContentOwnedRow(sheet)
        var num = 0

        while (firstContentOwnedRow.getCell(num) != null) {
            val currentCell = firstContentOwnedRow.getCell(num)
            val title = getCellValueAsString(currentCell)

            titleAndPositionMapList.add(mapOf("title" to title, "position" to num))
            ++num
        }
        return titleAndPositionMapList
    }

    private fun getContentOwnedRow(sheet: Sheet): Row {
        return try {
            var num = 0
            if (sheet.getRow(num) == null){
                while (sheet.getRow(num) != null) { num++ }
                sheet.getRow(num)
            } else {
                sheet.getRow(num)
            }
        } catch (e :NullPointerException) {
            sheet.getRow(sheet.firstRowNum)
        }
    }

    private fun getCellValueAsString(cell: Cell): String {
        return when (cell.cellType){
            CellType.STRING -> cell.stringCellValue
            CellType.NUMERIC -> {
                var text = cell.numericCellValue.toString()
                if (isIntegerValue(text)) {
                    text = changeDoubleIntoInt(text).toString()
                }
                text
            }
            CellType.FORMULA -> {
                try {
                    val text = (cell.richStringCellValue ?: "ERROR").toString()
                    text
                } catch (e : IllegalStateException) {
                    //文字列で無ければ、IllegalStateExceptionを起こすので
                    //数値であるかどうかの判定をこちらで行う。
                    var text = cell.numericCellValue.toString()
                    if (isIntegerValue(text)) {
                        text = changeDoubleIntoInt(text).toString()
                    }
                    text
                }
            }
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.ERROR -> cell.errorCellValue.toString()
            else -> ""
        }
    }

    private fun changeDoubleIntoInt(value: String): Int =
        value.toDouble().toInt()

    private fun isIntegerValue(value: String): Boolean {
        return try {
            value.toDouble()
            value.endsWith(".0")
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun getTitlesList(map: ArrayList<Map<String, Any>>): ArrayList<String> {
        val titlesList = arrayListOf<String>()

        for (num in 0..map.lastIndex) {
            val title = map[num]["title"] as String
            titlesList.add(title)
        }

        return titlesList
    }

    private fun decideWhichColumnToUseForNameBy(workbook: Workbook) {
        val sheetName = excelViewModel.sheetName
        val sheet = workbook.getSheet(sheetName)
        val titlesAndPositionMap = getTableTitlesAndPosition(sheet)

        val titlesList = getTitlesList(titlesAndPositionMap)

        val columnAsId = excelViewModel.columnAsId

        if (titlesList.contains(columnAsId)){
            titlesList.remove(columnAsId)
        }

        if (titlesList.isNotEmpty()) {
            createSelectExcelInfoDialogFrom(Excel.EXCEL_NAME_COLUMN, titlesList)
        }else{
            createErrorDialog(getString(R.string.excel_dialog_no_available_column_data))
        }

    }

    private fun getContentList(contentType: Excel): ArrayList<String> {
        val sheetName = excelViewModel.sheetName
        val sheet = excelViewModel.workbook.getSheet(sheetName)
        val titlesAndPositionMap = getTableTitlesAndPosition(sheet)

        val selection =
            when (contentType) {
                Excel.EXCEL_ID_COLUMN -> excelViewModel.columnAsId
                Excel.EXCEL_NAME_COLUMN -> excelViewModel.columnAsName
                else -> {
                    throw IllegalArgumentException("This argument is inappropriate for contentValue. Currently only \"EXCEL_ID_COLUMN\" or \"EXCEL_NAME_COLUMN\" is supported.")
                }
            }

        val contentOwnedRow = getContentOwnedRow(sheet)
        val selectedPosition: Int = getSelectedContentPosition(selection, titlesAndPositionMap)

        val contentList = arrayListOf<String>()

        //最初のタイトル行を除いた列の番号(firstRowNum)からスタートし、
        //値が入っている最後の列の番号(lastRowNum)までCellを取っていく。
        val firstRowNum = contentOwnedRow.rowNum + 1
        val lastRowNum = sheet.lastRowNum

        for (num in firstRowNum..lastRowNum) {
            //取得しようとした値が空白の場合、nullが返ってくるため、
            //NullPointerExceptionをキャッチ
            val cell: Cell? =
                try { sheet.getRow(num).getCell(selectedPosition) }
                catch (e: NullPointerException) { null }

            if (cell?.toString().isNullOrEmpty()){
                contentList.add("")
            }else{
                val content = getCellValueAsString(cell!!)
                contentList.add(content)
            }
        }
        return contentList
    }

    private fun setContentList(contentType: Excel, contentList: ArrayList<String>) {
        when (contentType) {
            Excel.EXCEL_ID_COLUMN -> {
                excelViewModel.setContentListForId(contentList)
            }
            Excel.EXCEL_NAME_COLUMN -> {
                excelViewModel.setContentListForName(contentList)
            }
            else -> {
                throw IllegalArgumentException("This argument is inappropriate for contentValue. Currently only \"EXCEL_ID_COLUMN\" or \"EXCEL_NAME_COLUMN\" is supported.")
            }
        }
    }

    private fun getSelectedContentPosition(selection: String, mapList: ArrayList<Map<String, Any>>): Int {
        var position = 0
        for (num in 0..mapList.lastIndex) {
            val map = mapList[num]
            if (map["title"] as String == selection) {
                position = map["position"] as Int
                break
            }
        }
        return position
    }

    private fun createSelectExcelInfoDialogFrom(
        excel: Excel,
        selection: ArrayList<String>,
    ) {
        val arguments = Bundle()
        arguments.putSerializable(BundleKey.ARG_EXCEL_KEY.name, excel)
        arguments.putStringArrayList(BundleKey.ARG_SELECTION_KEY.name, selection)

        val dialog = SelectExcelInfoDialog()
        dialog.arguments = arguments
        dialog.show(fragment.childFragmentManager, null)
    }

    private fun createErrorDialog(message: String){
        val dialog = MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.excel_dialog_error_title)
            .setMessage(message)
            .create()
        dialog.show()
    }

    private fun getString(id: Int, vararg: Any? = null): String{
        return if (vararg == null) {
            activity.applicationContext.getString(id)
        }else{
            activity.applicationContext.getString(id,vararg)
        }
    }
}