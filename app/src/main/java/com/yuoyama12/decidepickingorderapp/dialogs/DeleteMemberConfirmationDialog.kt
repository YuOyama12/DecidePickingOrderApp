package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import com.google.android.material.checkbox.MaterialCheckBox
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.DialogDeleteConfirmationBinding
import com.yuoyama12.decidepickingorderapp.preference.GeneralPreferenceFragment
import com.yuoyama12.decidepickingorderapp.viewmodels.MemberListViewModel

private const val CLASS_NAME = "DeleteMemberConfirmationDialog"
class DeleteMemberConfirmationDialog : DialogFragment() {

    private var _binding: DialogDeleteConfirmationBinding? = null
    private val binding: DialogDeleteConfirmationBinding
        get() = _binding!!

    private val memberListViewModel: MemberListViewModel by activityViewModels()

    companion object {
        private const val REQUEST_KEY = CLASS_NAME
        private const val RESULT_KEY_OK_CLICKED = "${CLASS_NAME}_OK_CLICKED"

        fun prepareFragmentResultListener(
            target: Fragment,
            onPositiveButtonClicked: () -> Unit
        ) {
            DeleteGroupConfirmationDialog().run {
                target.childFragmentManager
                    .setFragmentResultListener(REQUEST_KEY, target.viewLifecycleOwner) { requestKey, _ ->
                        if (requestKey != REQUEST_KEY) return@setFragmentResultListener

                        onPositiveButtonClicked.invoke()
                    }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val memberId = memberListViewModel.longClickedMemberId
        val memberName = memberListViewModel.longClickedMemberName
        val checkState = memberListViewModel.longClickedMemberCheckBox

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
                    setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY_OK_CLICKED to ""))

                    setCheckStateInPreference(binding.neverShowDialogAgainCheckbox)

                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    private fun setCheckStateInPreference(checkbox: MaterialCheckBox) {
        if (!checkbox.isChecked) {
            return
        } else {
            val sharedPref = GeneralPreferenceFragment.getSharedPreference(requireContext())
            val editor = sharedPref.edit()
            editor.putBoolean(
                getString(R.string.not_show_delete_confirmation_dialog_key),
                true
            ).apply()
        }
    }

}