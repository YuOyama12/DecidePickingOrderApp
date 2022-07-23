package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

private const val TITLE_KEY = "title"
private const val LAYOUT_KEY = "layout"
class InformationDialog : DialogFragment() {

    private var title = ""
    private var layoutId: Int = -1

    companion object {
        fun create(title: String, layoutId: Int): InformationDialog {
            return InformationDialog().apply {
                val bundle = Bundle()
                bundle.putString(TITLE_KEY, title)
                bundle.putInt(LAYOUT_KEY, layoutId)

                arguments = bundle
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        arguments?.let {
            title = it.getString(TITLE_KEY, "")
            layoutId = it.getInt(LAYOUT_KEY, -1)
        }

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(title)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    dialog.cancel()
                }

            if (layoutId != -1) {
                builder.setView(layoutId)
            }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }
}