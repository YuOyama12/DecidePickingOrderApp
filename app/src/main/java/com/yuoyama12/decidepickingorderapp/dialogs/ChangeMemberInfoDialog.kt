package com.yuoyama12.decidepickingorderapp.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.data.Member
import com.yuoyama12.decidepickingorderapp.databinding.DialogChangeMemberInfoBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.MemberListViewModel

class ChangeMemberInfoDialog : DialogFragment() {

    private var _binding: DialogChangeMemberInfoBinding? = null
    private val binding: DialogChangeMemberInfoBinding
        get() = _binding!!

    private val groupViewModel : GroupViewModel by activityViewModels()
    private val memberListViewModel: MemberListViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        val memberPrimaryKey = memberListViewModel.longClickedMemberPrimaryKey
        val memberId = memberListViewModel.longClickedMemberId
        val memberName = memberListViewModel.longClickedMemberName
        val checkState = memberListViewModel.longClickedMemberCheckBox

        _binding = DialogChangeMemberInfoBinding
            .inflate(requireActivity().layoutInflater)

        binding.memberId.setText(memberId.toString())
        binding.memberName.setText(memberName)
        binding.memberColorCheckBox.isChecked = checkState

        val dialog = requireActivity().let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle(R.string.menu_member_list_change_member_info_title)
                .setMessage(R.string.change_member_info_dialog_message)
                .setView(binding.root)

                .setPositiveButton(android.R.string.ok) { dialog, _ ->
                    val newId = getAppropriateMemberInfo(binding.memberId.text.toString(), memberId)
                    val newName =
                        getAppropriateMemberInfo(binding.memberName.text.toString(), memberName)
                    val newCheckState = binding.memberColorCheckBox.isChecked

                    if (newId == memberId &&
                        newName == memberName &&
                        newCheckState == checkState
                    ) {
                        dialog.cancel()
                    } else {
                        groupViewModel.updateMember(
                            Member(memberPrimaryKey, memberId, memberName, checkState),
                            Member(memberPrimaryKey, newId, newName, newCheckState)
                        )
                    }
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                }

            builder.create()

        } ?: throw  IllegalStateException("Activity cannot be null")

        return dialog
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getAppropriateMemberInfo(text: String, value: T): T {
        return when (text.isEmpty()) {
            true -> value
            false -> when (value) {
                is Int -> text.toInt() as T
                else -> text as T
            }
        }
    }

}
