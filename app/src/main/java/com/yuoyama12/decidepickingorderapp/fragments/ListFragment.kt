package com.yuoyama12.decidepickingorderapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.adapters.GroupListAdapter
import com.yuoyama12.decidepickingorderapp.adapters.MembersListAdapter
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.databinding.FragmentListBinding
import com.yuoyama12.decidepickingorderapp.dialogs.CreateNewGroupListDialog
import com.yuoyama12.decidepickingorderapp.dialogs.RenameGroupListDialog
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupListViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel : GroupViewModel by activityViewModels()
    private val groupListViewModel : GroupListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater,container,false)

        val groupListAdapter = GroupListAdapter(
            groupListViewModel,
            { group -> showMembers(group)  },
            { group -> moveToAddMemberFragment(group)}
        )

        val membersListAdapter = MembersListAdapter()

        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.list_action_bar_title)

        binding.groupListRecyclerView.adapter = groupListAdapter
        registerForContextMenu(binding.groupListRecyclerView)

        binding.memberListRecyclerView.adapter = membersListAdapter

        groupViewModel.groupList.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
            setNoItemNotificationTextState(it, binding.groupListNoItemText)
        }

        groupViewModel.membersList.observe(viewLifecycleOwner) {
            setNoItemNotificationTextState(it, binding.memberListNoItemText)
            membersListAdapter.submitList(it)
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

    override fun onCreateContextMenu(
        menu: ContextMenu,
        view: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, view, menuInfo)
        val inflater = MenuInflater(requireContext())
        inflater.inflate(R.menu.menu_group_list_item, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_group_list_rename -> {
                val dialog = RenameGroupListDialog()
                dialog.show(parentFragmentManager, null)
                true
            }
            R.id.menu_group_list_delete-> {
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showMembers(group: Group) {
        val groupId = group.groupId
        groupViewModel.setMembersListBy(groupId)
    }

    private fun moveToAddMemberFragment(group: Group) {
        val listName = group.name
        val id: Int = group.groupId
        val action = ListFragmentDirections
            .actionListFragmentToAddMemberFragment(listName, id)
        findNavController().navigate(action)
    }


    private fun <E> setNoItemNotificationTextState(
        list: List<E>,
        noItemTextView: TextView
    ) {
        when (list.isEmpty()) {
            true -> noItemTextView.visibility = VISIBLE
            false -> noItemTextView.visibility = GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}