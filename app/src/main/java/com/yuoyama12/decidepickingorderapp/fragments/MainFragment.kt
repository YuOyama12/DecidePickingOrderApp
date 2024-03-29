package com.yuoyama12.decidepickingorderapp.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yuoyama12.decidepickingorderapp.MainActivity
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.FragmentMainBinding
import com.yuoyama12.decidepickingorderapp.dialogs.SelectGroupDialog
import com.yuoyama12.decidepickingorderapp.viewmodels.GroupViewModel
import com.yuoyama12.decidepickingorderapp.viewmodels.OrderViewModel

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val groupViewModel: GroupViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        val actionBar = MainActivity.getActionBar(requireActivity())
        actionBar.visibility = View.VISIBLE
        actionBar.title = getString(R.string.app_name)
        actionBar.navigationIcon = null

        groupViewModel.groupList.observe(viewLifecycleOwner) {
            groupViewModel.hasAnyGroupsInGroupList.value = it.isNotEmpty()
        }

        groupViewModel.hasAnyGroupsInGroupList.observe(viewLifecycleOwner) {
            binding.noGroupNotification.visibility =
                when (it) {
                    true -> View.GONE
                    false -> View.VISIBLE
                }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.groupViewModel = groupViewModel
        binding.orderViewModel = orderViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.apply {
            startPickingOrderButton.setOnClickListener {
                showDialog(SelectGroupDialog())
            }

            showListButton.setOnClickListener{
                findNavController().navigate(R.id.action_mainFragment_to_listFragment)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.action_mainFragment_to_generalPreferenceFragment)
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog(dialogFragment: DialogFragment) {
        dialogFragment.show(childFragmentManager, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}