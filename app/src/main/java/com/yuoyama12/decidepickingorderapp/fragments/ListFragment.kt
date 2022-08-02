package com.yuoyama12.decidepickingorderapp.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.yuoyama12.decidepickingorderapp.ExcelDataProcessor
import com.yuoyama12.decidepickingorderapp.MainActivity
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.adapters.GroupListAdapter
import com.yuoyama12.decidepickingorderapp.adapters.MemberListAdapter
import com.yuoyama12.decidepickingorderapp.data.Group
import com.yuoyama12.decidepickingorderapp.data.Member
import com.yuoyama12.decidepickingorderapp.databinding.FragmentListBinding
import com.yuoyama12.decidepickingorderapp.dialogs.*
import com.yuoyama12.decidepickingorderapp.viewmodels.ExcelViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupListViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.MemberListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel : GroupViewModel by activityViewModels()
    private val groupListViewModel : GroupListViewModel by activityViewModels()
    private val memberListViewModel : MemberListViewModel by activityViewModels()
    private val excelViewModel : ExcelViewModel by activityViewModels()

    private var  _excelDataProcessor: ExcelDataProcessor? = null
    private val excelDataProcessor get() = _excelDataProcessor!!

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult? ->
        if (result?.resultCode == Activity.RESULT_OK) {
            excelDataProcessor.execute(result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater,container,false)

        val groupListAdapter = GroupListAdapter(
            groupListViewModel,
            { group -> showMembers(group) },
            { group -> moveToAddMemberFragment(group)}
        )

        val memberListAdapter = MemberListAdapter(memberListViewModel)

        binding.groupListRecyclerView.adapter = groupListAdapter
        registerForContextMenu(binding.groupListRecyclerView)

        binding.memberListRecyclerView.adapter = memberListAdapter
        registerForContextMenu(binding.memberListRecyclerView)

        groupViewModel.groupList.observe(viewLifecycleOwner) {
            groupListAdapter.submitList(it)
            setNoItemNotificationTextState(it, binding.groupListNoItemText)
        }

        groupViewModel.memberList.observe(viewLifecycleOwner) {
            setNoItemNotificationTextState(it, binding.memberListNoItemText)
            memberListAdapter.submitList(it)
            groupViewModel.setCurrentDisplayedMemberList(it)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _excelDataProcessor = ExcelDataProcessor(this, requireActivity(), excelViewModel)

        CreateFromExcelDataDialog.prepareFragmentResultListener(this) {
            openFileProvider()
        }

        DeleteGroupConfirmationDialog.prepareFragmentResultListener(this) {
            deleteGroup()
        }

        DeleteMemberConfirmationDialog.prepareFragmentResultListener(this) {
            deleteMember()
        }

        binding.createNewGroupListButton.setOnClickListener {
            showDialog(CreateNewGroupListDialog())
        }

        binding.importFromExcelButton.setOnClickListener {
            showDialog(CreateFromExcelDataDialog())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)

        val actionBar = MainActivity.getActionBar(requireActivity())
        actionBar.title = getString(R.string.list_action_bar_title)

        MainActivity.createNavigationIcon(actionBar) {
            findNavController().navigate(R.id.action_listFragment_to_mainFragment)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_group_list -> {
                val groupSortingMethods = resources.getStringArray(R.array.array_sort_group_list)
                showSortDialog(SortObject.GROUP, groupSortingMethods)
                true
            }
            R.id.sort_member_list -> {
                val memberSortingMethods = resources.getStringArray(R.array.array_sort_member_list)
                showSortDialog(SortObject.MEMBER, memberSortingMethods)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        view: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, view, menuInfo)
        val inflater = MenuInflater(requireContext())

        val menuResourceId =
            when (view) {
                binding.memberListRecyclerView -> R.menu.menu_member_list_item
                else -> R.menu.menu_group_list_item
            }

        inflater.inflate(menuResourceId, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_group_list_rename -> {
                showDialog(RenameGroupListDialog())
                true
            }
            R.id.menu_group_list_delete -> {
                if (notShowDialogOnPreference()){
                    deleteGroup()
                } else {
                    showDialog(DeleteGroupConfirmationDialog())
                }
                true
            }

            R.id.menu_member_list_change_member_info -> {
                showDialog(ChangeMemberInfoDialog())
                true
            }
            R.id.menu_member_list_delete -> {
                if (notShowDialogOnPreference()){
                    deleteMember()
                } else {
                    showDialog(DeleteMemberConfirmationDialog())
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showSortDialog(sortObject: SortObject, sortingMethods: Array<String>) {
        val dialog = SortDialog.create(sortObject, sortingMethods)
        dialog.show(childFragmentManager, null)
    }

    private fun showMembers(group: Group) {
        val groupId = group.groupId
        groupViewModel.setMemberListBy(groupId)
    }

    private fun moveToAddMemberFragment(group: Group) {
        showMembers(group)

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

    private fun openFileProvider() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.ms-excel"
        }
        startForResult.launch(intent, null)
    }

    private fun notShowDialogOnPreference(): Boolean {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        return sharedPref.getBoolean(
            getString(R.string.not_show_delete_confirmation_dialog_key),
            false
        )
    }

    private fun showDialog(dialogFragment: DialogFragment) {
        dialogFragment.show(childFragmentManager, null)
    }

    private fun deleteGroup() {
        val groupId = groupListViewModel.longClickedGroupId

        groupViewModel.deleteGroup(groupId)

        groupViewModel.resetMemberList()
        groupListViewModel.resetSelectedStateInfo()

        showDeleteCompleteMessage()
    }

    private fun deleteMember() {
        val memberPrimaryKey = memberListViewModel.longClickedMemberPrimaryKey
        val memberId = memberListViewModel.longClickedMemberId
        val memberName = memberListViewModel.longClickedMemberName
        val checkState = memberListViewModel.longClickedMemberCheckBox

        val member = Member(memberPrimaryKey, memberId, memberName, checkState)
        groupViewModel.deleteMember(memberPrimaryKey, member)

        showDeleteCompleteMessage()
    }

    private fun showDeleteCompleteMessage() {
        val deleteCompletedMsg = getString(R.string.delete_completed)
        val view = requireParentFragment().requireView()
        Snackbar.make(view, deleteCompletedMsg, Snackbar.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        _excelDataProcessor = null
    }


}