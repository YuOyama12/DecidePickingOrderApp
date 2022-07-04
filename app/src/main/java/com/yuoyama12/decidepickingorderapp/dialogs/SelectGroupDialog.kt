package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.OrderViewModel

class SelectGroupDialog : DialogFragment() {

    private val groupViewModel: GroupViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val groupList = groupViewModel.groupList.value!!
        val groupNameList = groupList.map { it.name }.toTypedArray()

        var selectedGroup: Group? = null

        val noMemberMsg = getString(R.string.no_member_message)

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.select_group_dialog_title)
                .setSingleChoiceItems(groupNameList, -1) { _, which ->
                    selectedGroup = groupList[which]
                }
                .setPositiveButton(android.R.string.ok) { dialog,_ ->
                    if (selectedGroup != null) {

                        if (hasNoMember(selectedGroup!!)) {
                            val view = requireParentFragment().requireView()
                            Snackbar.make(view, noMemberMsg, Snackbar.LENGTH_SHORT)
                                .show()
                            return@setPositiveButton
                        }

                        if (selectedGroup!! != orderViewModel.selectedGroup ||
                            ifAscendingOrderCheckStateChanged() ||
                            orderViewModel.selectedGroup == null) {

                            orderViewModel.resetCurrentItemPosition()
                            orderViewModel.setSelectedGroup(selectedGroup!!)
                            orderViewModel.createMemberList()

                        }

                        requireParentFragment().findNavController()
                            .navigate(R.id.action_mainFragment_to_orderDisplayFragment)
                    } else {
                        dialog.cancel()
                    }
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    private fun hasNoMember(group: Group): Boolean {
        return group.members.isEmpty()
    }

    private fun ifAscendingOrderCheckStateChanged(): Boolean {
        return orderViewModel.ascendingOrderCheckState.value !=
                orderViewModel.restoredAscendingOrderCheckState
    }

}
