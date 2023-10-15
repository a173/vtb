package com.example.vtb.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import com.example.vtb.MainActivity
import com.example.vtb.R
import com.example.vtb.databinding.FragmentScreenFromRealAppBinding
import com.example.vtb.databinding.FragmentTalonBinding
import com.example.vtb.interfaces.ExpressInterface
import com.example.vtb.interfaces.ServiceInterface
import com.example.vtb.models.DepartmentWithTime

class TalonFragment : Fragment() {

    private lateinit var binding: FragmentTalonBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTalonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val department = arguments?.getSerializable("department") as DepartmentWithTime?
        val isMan = arguments?.getBoolean("isMan") ?: false
        val letters = listOf('A', 'B', 'C', 'D')
        val randomIndex = (letters.indices).random()
        val randomNumber = letters[randomIndex] + String.format("%03d", (0..999).random())
        if (department != null) {
            binding.service.text = (activity as ServiceInterface).getService().text
            binding.address.text = department.address
            binding.number.text = randomNumber
        }
        binding.btnWalk.isGone = arguments?.getBoolean("button")?.not() ?: false
        binding.btnWalk.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("department", department)
            bundle.putBoolean("one point", true)
            bundle.putBoolean("isMan", isMan)
            (activity as MainActivity).navController.navigate(R.id.action_talonFragment_to_yandexMap, bundle)
        }
    }

}