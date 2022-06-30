package com.yuoyama12.decidepickingorderapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.FragmentAddMemberBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel

class AddMemberFragment : Fragment() {

    private var _binding : FragmentAddMemberBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel : GroupViewModel by activityViewModels()

    private val args: AddMemberFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.add_member_action_bar_title, args.listName)
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
        binding.addButton.setOnClickListener {
            insertMemberIntoGroup(args.id)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_add_member, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.action_addMemberFragment_to_listFragment)
        return super.onOptionsItemSelected(item)
    }

    private fun insertMemberIntoGroup(groupId: Int) {
        val memberId = binding.memberId.text.toString()
        val memberName = binding.memberName.text.toString()
        val isChecked = binding.memberColorCheckBox.isChecked

        if (memberName.isNotEmpty()) {
            groupViewModel.insertMemberIntoGroup(groupId, memberId, memberName, isChecked)

            groupViewModel.groupList.observe(viewLifecycleOwner) {
                val message = getString(R.string.add_member_completed)
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

                groupViewModel.setMemberListBy(groupId)
            }

            resetAllInputFields()

        } else {
            val message = getString(R.string.add_member_error_message)
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
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