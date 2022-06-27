package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogGroupListNameBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupListViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel

class RenameGroupListDialog : DialogFragment() {

    private var _binding: DialogGroupListNameBinding? = null
    private val binding: DialogGroupListNameBinding
        get() = _binding!!

    private val groupViewModel: GroupViewModel by activityViewModels()
    private val groupListViewModel: GroupListViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val originalName = groupListViewModel.longClickedGroupName
        val groupId = groupListViewModel.longClickedGroupId

        _binding = DialogGroupListNameBinding
            .inflate(requireActivity().layoutInflater)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(getString(R.string.rename_group_list_dialog_title))
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    val listName = binding.newGroupListName.text.toString().trim()

                    if (listName.isNotEmpty() && listName != originalName) {
                        groupViewModel.updateGroupName(groupId, listName)
                    } else {
                        dialog.cancel()
                    }
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

            binding.newGroupListName.setText(originalName)

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }
}