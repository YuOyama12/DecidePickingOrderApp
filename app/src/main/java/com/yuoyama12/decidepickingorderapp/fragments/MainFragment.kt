package com.yuoyama12.decidepickingorderapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yuoyama12.decidepickingorderapp.R
import com.yuoyama12.decidepickingorderapp.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        val actionBar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.action_bar)
        actionBar?.title = getString(R.string.app_name)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.showListButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_listFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}