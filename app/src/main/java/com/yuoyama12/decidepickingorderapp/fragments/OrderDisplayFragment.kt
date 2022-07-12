package com.yuoyama12.decidepickingorderapp.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.yuoyama12.decidepickingorderapp.FlickListener
import com.yuoyama12.decidepickingorderapp.databinding.FragmentOrderDisplayBinding
import com.yuoyama12.decidepickingorderapp.viewmodels.OrderViewModel


class OrderDisplayFragment : Fragment() {

    private var _binding : FragmentOrderDisplayBinding? = null
    private val binding get() = _binding!!

    private val orderViewModel: OrderViewModel by activityViewModels()

    private val flickListener: FlickListener.Listener = object : FlickListener.Listener{
        override fun onFlickToLeft() {
            orderViewModel.goPreviousItem()
        }
        override fun onFlickToRight() {
            orderViewModel.goNextItem()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDisplayBinding.inflate(inflater, container, false)
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.orderViewModel = orderViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        view.setOnTouchListener(FlickListener(flickListener))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}