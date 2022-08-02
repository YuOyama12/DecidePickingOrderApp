package com.yuoyama12.decidepickingorderapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.yuoyama12.decidepickingorderapp.MainActivity
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.FragmentAddMemberBinding
import com.yuoyama12.decidepickingorderapp.dialogs.InformationDialog
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel


class AddMemberFragment : Fragment() {

    private var _binding : FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel : GroupViewModel by activityViewModels()

    private val args: AddMemberFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAddMemberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notificationColorHelp.setOnClickListener {
            val title = getString(R.string.what_notification_color_is_text).removeBulletPoint()

            InformationDialog
                .create(title, R.layout.dialog_about_notification_color)
                .show(childFragmentManager, null)
        }

        binding.addButton.setOnClickListener {
            insertMemberIntoGroup(args.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        
        val actionBar = MainActivity.getActionBar(requireActivity())
        actionBar.title = getString(R.string.add_member_action_bar_title, args.listName)

        MainActivity.createNavigationIcon(actionBar) {
            findNavController().navigate(R.id.action_addMemberFragment_to_listFragment)
        }
    }

    private fun insertMemberIntoGroup(groupId: Int) {
        val memberId = binding.memberId.text.toString()
        val memberName = binding.memberName.text.toString()
        val isChecked = binding.memberColorCheckBox.isChecked

        if (memberName.isNotEmpty()) {
            groupViewModel.insertMemberIntoGroup(groupId, memberId, memberName, isChecked)

            groupViewModel.groupList.observe(viewLifecycleOwner) {
                groupViewModel.setMemberListBy(groupId)
            }

            val message = getString(R.string.add_member_completed)
            showSnackBarBelowActionbar(message)

            resetAllInputFields()

        } else {
            val message = getString(R.string.add_member_error_message)
            showSnackBarBelowActionbar(message)
        }
    }

    private fun showSnackBarBelowActionbar(message: String) {
        val snackBar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        val snackBarLayoutParams = snackBarView.layoutParams

        val layoutParams = LinearLayout.LayoutParams(
            snackBarLayoutParams.width,
            snackBarLayoutParams.height
        )

        val actionBar = MainActivity.getActionBar(requireActivity())

        with(layoutParams) {
            this.setMargins(0, actionBar.height + 3, 0, 0)
            this.gravity = Gravity.CENTER_HORIZONTAL
        }

        snackBarView.layoutParams = layoutParams
        snackBar.show()
    }

    private fun resetAllInputFields() {
        binding.memberId.setText("")
        binding.memberName.setText("")
        binding.memberColorCheckBox.isChecked = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

fun String.removeBulletPoint(): String {
    return if (this.first() == '・' || this.first() == '･') {
        this.substring(1)
    } else {
        this
    }
}


