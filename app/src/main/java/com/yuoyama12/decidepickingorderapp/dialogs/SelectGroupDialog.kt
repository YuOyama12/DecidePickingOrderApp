package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel

class SelectGroupDialog : DialogFragment() {

    private val groupViewModel: GroupViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        var groupNameList = listOf<String>()
        groupViewModel.groupList.observe(requireParentFragment().viewLifecycleOwner) {
            groupNameList = getGroupNameListFrom(it)
        }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.select_dialog_singlechoice,
            groupNameList
        )

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.select_group_dialog_title)
                .setSingleChoiceItems(adapter,-1) { dialog, position ->
                    val selectedGroup = adapter.getItem(position).toString()

                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    private fun getGroupNameListFrom(groupList: List<Group>): List<String> {
        val groupNameList = mutableListOf<String>()
        for (group in groupList) {
            groupNameList.add(group.name)
        }

        return groupNameList
    }
}
