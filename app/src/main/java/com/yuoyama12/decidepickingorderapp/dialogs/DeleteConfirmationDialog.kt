package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogDeleteConfirmationBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupListViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel

class DeleteConfirmationDialog : DialogFragment() {

    private var _binding: DialogDeleteConfirmationBinding? = null
    private val binding: DialogDeleteConfirmationBinding
        get() = _binding!!

    private val groupViewModel: GroupViewModel by activityViewModels()
    private val groupListViewModel: GroupListViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val groupName = groupListViewModel.longClickedGroupName
        val groupId = groupListViewModel.longClickedGroupId

        val deleteCompletedMsg = getString(R.string.delete_completed)

        _binding = DialogDeleteConfirmationBinding
            .inflate(requireActivity().layoutInflater)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(getString(R.string.delete_dialog_title))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    groupViewModel.deleteGroup(groupId)

                    groupViewModel.resetMembersList()
                    groupListViewModel.resetSelectedPosition()

                    val view = requireParentFragment().requireView()
                    Snackbar.make(view, deleteCompletedMsg, Snackbar.LENGTH_SHORT)
                        .show()

                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

            binding.deleteConfirmationMessage.text = getString(R.string.delete_confirmation_dialog_message, groupName)

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }
}