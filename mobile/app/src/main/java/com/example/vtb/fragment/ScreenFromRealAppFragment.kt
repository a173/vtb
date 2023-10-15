package com.example.vtb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vtb.MainActivity
import com.example.vtb.R
import com.example.vtb.databinding.FragmentScreenFromRealAppBinding
import com.example.vtb.databinding.FragmentYandexMapBinding
import com.example.vtb.interfaces.ExpressInterface

class ScreenFromRealAppFragment : Fragment() {

    private lateinit var binding: FragmentScreenFromRealAppBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScreenFromRealAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNoExpress.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_screenFromRealAppFragment_to_filterAndRegistrationFragment)
            (activity as ExpressInterface).setExpress(false)
        }
        binding.btnExpress.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_screenFromRealAppFragment_to_filterAndRegistrationFragment)
            (activity as ExpressInterface).setExpress(true)
        }
    }

}