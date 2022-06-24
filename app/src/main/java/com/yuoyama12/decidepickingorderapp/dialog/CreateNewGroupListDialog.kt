package com.yuoyama12.decidepickingorderapp.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogCreateNewGroupListBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel

class CreateNewGroupListDialog : DialogFragment() {

    private var _binding: DialogCreateNewGroupListBinding? = null
    private val binding: DialogCreateNewGroupListBinding
        get() = _binding!!

    private val groupViewModel: GroupViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        _binding = DialogCreateNewGroupListBinding
            .inflate(requireActivity().layoutInflater)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(getString(R.string.create_new_group_list_dialog_title))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    val listName = binding.newGroupListName.text.toString().trim()

                    if (listName.isNotEmpty()) groupViewModel.insertGroup(listName)
                    else dialog.cancel()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }
}
