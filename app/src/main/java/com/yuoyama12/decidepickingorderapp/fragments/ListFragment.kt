package com.yuoyama12.decidepickingorderapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.adapters.GroupListAdapter
import com.yuoyama12.decidepickingorderapp.adapters.MembersListAdapter
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.databinding.FragmentListBinding
import com.yuoyama12.decidepickingorderapp.dialog.CreateNewGroupListDialog
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel : GroupViewModel by activityViewModels()

    private val groupListAdapter = GroupListAdapter(
        { group -> showMembers(group)  },
        { group -> moveToAddMemberFragment(group)}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater,container,false)

        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.list_action_bar_title)

        binding.groupListRecyclerView.adapter = groupListAdapter

        groupViewModel.groupList.observe(viewLifecycleOwner){
            groupListAdapter.submitList(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createNewGroupListButton.setOnClickListener {
            val dialog = CreateNewGroupListDialog()
            dialog.show(parentFragmentManager, null)
        }

    }

    private fun showMembers(group: Group) {
        val groupId = group.groupId

        groupViewModel.setMembersListBy(groupId)

        groupViewModel.membersList.observe(viewLifecycleOwner){
            val adapter = MembersListAdapter()
            binding.memberListRecyclerView.adapter = adapter
            adapter.submitList(it)
        }
    }

    private fun moveToAddMemberFragment(group: Group) {
        val listName = group.name
        val id: Int = group.groupId
        val action = ListFragmentDirections
            .actionListFragmentToAddMemberFragment(listName, id)
        findNavController().navigate(action)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}