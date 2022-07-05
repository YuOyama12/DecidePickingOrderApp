package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogWhenCreateFromExcelBinding

class CreateFromExcelDataDialog : DialogFragment() {

    private var _binding: DialogWhenCreateFromExcelBinding? = null
    private val binding: DialogWhenCreateFromExcelBinding
        get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        _binding = DialogWhenCreateFromExcelBinding
            .inflate(requireActivity().layoutInflater)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.excel_dialog_message_title)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }
}