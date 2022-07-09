package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.yuoyama12.decidepickingorderapp.BundleKey
import com.yuoyama12.decidepickingorderapp.Excel
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.viewmodels.ExcelViewModel

private const val CLASS_NAME = "SelectExcelInfoDialog"
class SelectExcelInfoDialog : DialogFragment() {

    private  val excelViewModel: ExcelViewModel by activityViewModels()

    companion object {
        private const val REQUEST_KEY = CLASS_NAME
        private const val RESULT_KEY_SHEET_SELECTED = "${CLASS_NAME}_SHEET_SELECTED"
        private const val RESULT_KEY_ID_SELECTED = "${CLASS_NAME}_ID_SELECTED"

        fun prepareFragmentResultListener(
            target: Fragment,
            onSheetSelected: () -> Unit,
            onIdSelected: () -> Unit
        ) {
            SelectExcelInfoDialog().run {
                target
                    .childFragmentManager
                    .setFragmentResultListener(REQUEST_KEY, target.viewLifecycleOwner) { requestKey, bundle ->
                        if (requestKey != REQUEST_KEY) return@setFragmentResultListener

                        when {
                            bundle.containsKey(RESULT_KEY_SHEET_SELECTED) -> onSheetSelected.invoke()
                            bundle.containsKey(RESULT_KEY_ID_SELECTED) -> onIdSelected.invoke()

                        }
                    }
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val excel = arguments?.get(BundleKey.ARG_EXCEL_KEY.name) as Excel
        val selection = arguments?.get(BundleKey.ARG_SELECTION_KEY.name) as ArrayList<*>

        val adapter = ArrayAdapter(requireContext(),
            android.R.layout.select_dialog_singlechoice,
            selection)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)
                .setCancelable(false)
                .setTitle(getDialogTitle(excel))
                .setSingleChoiceItems(adapter,-1) { dialog, position ->
                    val item = adapter.getItem(position).toString()

                    setExcelInfo(excel, item)
                    setFragmentResultFrom(excel)

                    dialog.dismiss()
                }

            builder.create()
        }

        return dialog
    }

    private fun getDialogTitle(excel: Excel): String {
        return when (excel) {
            Excel.EXCEL_SHEET -> getString(R.string.excel_dialog_select_sheet_title)
            Excel.EXCEL_ID_COLUMN -> getString(R.string.excel_dialog_select_id_title)
            Excel.EXCEL_NAME_COLUMN -> getString(R.string.excel_dialog_select_name_title)
        }
    }

    private fun setFragmentResultFrom(excel: Excel) {
        when (excel) {
            Excel.EXCEL_SHEET ->
                setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY_SHEET_SELECTED to ""))
            Excel.EXCEL_ID_COLUMN ->
                setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY_ID_SELECTED to ""))
        }
    }

    private fun setExcelInfo(excel: Excel, item: String) {
        when (excel) {
            Excel.EXCEL_SHEET ->
                excelViewModel.setSheetName(item)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        clearFragmentResult(REQUEST_KEY)
    }

}
