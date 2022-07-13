package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.snackbar.Snackbar
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.data.Member
import com.yuoyama12.decidepickingorderapp.databinding.DialogDeleteConfirmationBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.MemberListViewModel

class DeleteMemberConfirmationDialog : DialogFragment() {

    private var _binding: DialogDeleteConfirmationBinding? = null
    private val binding: DialogDeleteConfirmationBinding
        get() = _binding!!

    private val groupViewModel: GroupViewModel by activityViewModels()
    private val memberListViewModel: MemberListViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val memberPrimaryKey = memberListViewModel.longClickedMemberPrimaryKey
        val memberId = memberListViewModel.longClickedMemberId
        val memberName = memberListViewModel.longClickedMemberName
        val checkState = memberListViewModel.longClickedMemberCheckBox

        val deleteCompletedMsg = getString(R.string.delete_completed)

        _binding = DialogDeleteConfirmationBinding
            .inflate(requireActivity().layoutInflater)

        binding.apply {
            deleteConfirmationMessage.text =
                getString(R.string.delete_member_confirmation_dialog_message)

            memberItem.apply {
                root.apply {
                    visibility = View.VISIBLE
                    isEnabled = false
                    background = ResourcesCompat.getDrawable(resources, R.drawable.border, null)
                }

                listItemMemberDivider.visibility = View.GONE
                listItemMemberId.text = memberId.toString()
                listItemMemberName.text = memberName
                listItemMemberCheckBox.isChecked = checkState
            }
        }

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.delete_dialog_title)
                .setView(binding.root)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val member = Member(memberPrimaryKey, memberId, memberName, checkState)
                    groupViewModel.deleteMember(memberPrimaryKey, member)

                    val view = requireParentFragment().requireView()
                    Snackbar.make(view, deleteCompletedMsg, Snackbar.LENGTH_SHORT)
                        .show()

                    setPreferenceIfChecked(binding.neverShowDialogAgainCheckbox)

                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    private fun setPreferenceIfChecked(checkbox: MaterialCheckBox) {
        if (!checkbox.isChecked) {
            return
        } else {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val editor = sharedPref.edit()
            editor.putBoolean(
                getString(R.string.show_delete_confirmation_dialog_preference_key),
                true
            ).apply()
        }

    }
}