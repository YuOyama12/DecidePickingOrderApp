package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogWhenCreateFromExcelBinding

private const val CLASS_NAME = "CreateFromExcelDataDialog"
class CreateFromExcelDataDialog : DialogFragment() {

    private var _binding: DialogWhenCreateFromExcelBinding? = null
    private val binding: DialogWhenCreateFromExcelBinding
        get() = _binding!!

    companion object {
        private const val REQUEST_KEY = CLASS_NAME
        private const val RESULT_KEY_POSITIVE_CLICKED = "${CLASS_NAME}_POSITIVE_CLICKED"

        fun prepareFragmentResultListener(
            target: Fragment,
            onPositiveButtonClicked: (() -> Unit)? = null
        ) {
            CreateFromExcelDataDialog().run {
                target
                    .childFragmentManager
                    .setFragmentResultListener(REQUEST_KEY, target.viewLifecycleOwner) { requestKey, bundle ->
                        if (requestKey != REQUEST_KEY) return@setFragmentResultListener

                        when {
                            bundle.containsKey(RESULT_KEY_POSITIVE_CLICKED) -> {
                                onPositiveButtonClicked?.invoke()
                            }
                        }
                    }
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        _binding = DialogWhenCreateFromExcelBinding
            .inflate(requireActivity().layoutInflater)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.excel_dialog_message_title)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY_POSITIVE_CLICKED to ""))
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        clearFragmentResult(REQUEST_KEY)
    }

}