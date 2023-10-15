package com.example.vtb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.vtb.MainActivity
import com.example.vtb.R
import com.example.vtb.adapters.ResultRecyclerAdaptor
import com.example.vtb.databinding.FragmentFilterAndRegistrationBinding
import com.example.vtb.enums.FilterAndRegistrationScreenState
import com.example.vtb.enums.ServiceType
import com.example.vtb.interfaces.DepartmentInterface
import com.example.vtb.interfaces.ExpressInterface
import com.example.vtb.interfaces.ListenerDepartment
import com.example.vtb.interfaces.PointInterface
import com.example.vtb.interfaces.ServiceInterface
import com.example.vtb.models.DepartmentWithTime
import com.example.vtb.models.ModelToBackCar
import com.example.vtb.models.ModelToBackMen
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingSummarySession
import com.yandex.mapkit.directions.driving.Summary
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.SummarySession
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetTime

class FilterAndRegistrationFragment : Fragment(), ListenerDepartment {

    private lateinit var point: Point
    private lateinit var binding: FragmentFilterAndRegistrationBinding
    private lateinit var listRadioButton: List<AppCompatRadioButton>
    private var stateScreen = FilterAndRegistrationScreenState.EMPTY
        set(value) {
            field = value
            updateState(value)
        }
    private lateinit var adaptorCar: ResultRecyclerAdaptor
    private lateinit var adaptorMan: ResultRecyclerAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        binding = FragmentFilterAndRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateScreen = FilterAndRegistrationScreenState.FACE
        onBackPressed()
        listenerRadioButton()
        binding.btnApply.setOnClickListener {
            stateScreen = FilterAndRegistrationScreenState.SERVICE
        }
        binding.btnApplyStage2.setOnClickListener {
            if ((activity as ExpressInterface).getExpress()) {
                (activity as ServiceInterface).setService(getServiceType())
                stateScreen = FilterAndRegistrationScreenState.CHOICE
                detectLocationUser()
            } else {
                val bundle = Bundle()
                bundle.putBoolean("button", false)
                (activity as MainActivity).navController.navigate(
                    R.id.action_filterAndRegistrationFragment_to_yandexMap, bundle)
            }
        }
        adaptorCar = ResultRecyclerAdaptor(false, this)
        adaptorMan = ResultRecyclerAdaptor(true, this)
        binding.recyclerMan.adapter = adaptorMan
        binding.titleMan.setOnClickListener {
            binding.recyclerMan.isGone = !binding.recyclerMan.isGone
            if (binding.recyclerMan.isGone) {
                binding.ivArrowMan.setImageResource(R.drawable.arrow_down)
            } else {
                binding.ivArrowMan.setImageResource(R.drawable.arrow_up)
            }
        }
        binding.recyclerCar.adapter = adaptorCar
        binding.titleCar.setOnClickListener {
            binding.recyclerCar.isGone = !binding.recyclerCar.isGone
            if (binding.recyclerCar.isGone) {
                binding.ivArrowCar.setImageResource(R.drawable.arrow_down)
            } else {
                binding.ivArrowCar.setImageResource(R.drawable.arrow_up)
            }
        }
    }

    private fun getServiceType(): ServiceType {
        if (binding.option1.isChecked) return ServiceType.CARDS
        if (binding.option2.isChecked) return ServiceType.INVESTING
        if (binding.option3.isChecked) return ServiceType.CREDIT
        if (binding.option4.isChecked) return ServiceType.IPOTEKA
        return ServiceType.CHANGE
    }

    private fun updateState(state: FilterAndRegistrationScreenState) {
        when (state) {
            FilterAndRegistrationScreenState.EMPTY -> {}
            FilterAndRegistrationScreenState.FACE -> {
                binding.faceFilter.isVisible = true
                binding.serviceFilter.isGone = true
                binding.resultFilter.isGone = true
            }
            FilterAndRegistrationScreenState.SERVICE -> {
                binding.faceFilter.isGone = true
                binding.serviceFilter.isVisible = true
                binding.resultFilter.isGone = true
            }
            FilterAndRegistrationScreenState.CHOICE -> {
                binding.faceFilter.isGone = true
                binding.serviceFilter.isGone = true
                binding.resultFilter.isVisible = true
            }
            FilterAndRegistrationScreenState.RESULT -> {}
        }
    }

    private fun listenerRadioButton() {
        with(binding) {
            listRadioButton = listOf(rbLawerPerson, rbPrime, rbPrivilege, rbPhisicPerson)
            listRadioButton.forEach {
                it.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) resetRadioButtons(buttonView)
                }
            }
            llLawerPerson.setOnClickListener {
                resetRadioButtons(rbLawerPerson)
            }
            llPrime.setOnClickListener {
                resetRadioButtons(rbPrime)
            }
            llPrivilege.setOnClickListener {
                resetRadioButtons(rbPrivilege)
            }
            llPhisicPerson.setOnClickListener {
                resetRadioButtons(rbPhisicPerson)
            }
            llVip.setOnClickListener {
                val check = rbVipCheck.isChecked
                val uncheck = rbVipUncheck.isChecked
                rbVipCheck.isChecked = !check
                rbVipUncheck.isChecked = !uncheck
            }
        }
    }

    private fun resetRadioButtons(button: CompoundButton) {
        with(binding) {
            val listRadioButton = listOf(rbLawerPerson, rbPrime, rbPrivilege, rbPhisicPerson)
            for (radioButton in listRadioButton) {
                radioButton.isChecked = radioButton == button
            }
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (stateScreen) {
                    FilterAndRegistrationScreenState.EMPTY -> {}
                    FilterAndRegistrationScreenState.FACE -> (activity as MainActivity).navController.navigate(
                        R.id.action_filterAndRegistrationFragment_to_screenFromRealAppFragment)
                    FilterAndRegistrationScreenState.SERVICE -> stateScreen = FilterAndRegistrationScreenState.FACE
                    FilterAndRegistrationScreenState.CHOICE -> stateScreen = FilterAndRegistrationScreenState.SERVICE
                    FilterAndRegistrationScreenState.RESULT -> stateScreen = FilterAndRegistrationScreenState.CHOICE
                }
            }
        })
    }

    override fun onClick(department: DepartmentWithTime, isMan: Boolean) {
        val bundle = Bundle()
        bundle.putSerializable("department", department)
        bundle.putBoolean("button", true)
        bundle.putBoolean("isMan", isMan)
        (activity as PointInterface).setPoint(point)
        (activity as MainActivity).navController.navigate(R.id.talonFragment,bundle)
    }

    private fun detectLocationUser() {
        LocationServices.getFusedLocationProviderClient(requireContext())
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener {
                point = Point(it.result.latitude, it.result.longitude)
                val drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
                val pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter()
                val departments = (activity as DepartmentInterface).getDepartments()
                val countRequest = departments.size
                val listCar = mutableListOf<ModelToBackCar>()
                val listMen = mutableListOf<ModelToBackMen>()
                var indexCar = 0
                var indexMan = 0
                for (department in departments) {
                    val modelCar = ModelToBackCar(department.id, null)
                    val modelMan = ModelToBackMen(department.id, null)
                    val requestPoints = buildList {
                        add(RequestPoint(Point(it.result.latitude, it.result.longitude), RequestPointType.WAYPOINT, null))
                        add(RequestPoint(Point(department.latitude!!, department.longitude!!), RequestPointType.WAYPOINT, null))
                    }
                    val drivingOptions = DrivingOptions()
                    val vehicleOptions = VehicleOptions()
                    val timeOptions = TimeOptions()
                    drivingRouter.requestRoutesSummary(
                        requestPoints,
                        drivingOptions,
                        vehicleOptions,
                        object : DrivingSummarySession.DrivingSummaryListener {
                            override fun onDrivingSummaries(p0: MutableList<Summary>) {
                                modelCar.timeCar = p0.first().weight.timeWithTraffic.value
                                listCar.add(modelCar)
                                indexCar++
                                if (indexCar == countRequest) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.Main) {
                                            adaptorCar.setList(departments.filter { it.id == 1 }
                                                .map { department ->
                                                    DepartmentWithTime(department.id,
                                                        department.salePointName,
                                                        department.address,
                                                        department.latitude,
                                                        department.longitude, "Ваша очередь подойдет через 5 минут")
                                                })
                                        }

                                    }
                                }
                            }
                            override fun onDrivingSummariesError(p0: Error) {}
                        },
                    )
                    pedestrianRouter.requestRoutesSummary(
                        requestPoints,
                        timeOptions,
                        object : SummarySession.SummaryListener {
                            override fun onMasstransitSummaries(p0: MutableList<com.yandex.mapkit.transport.masstransit.Summary>) {
                                modelMan.timeMen = p0.first().weight.time.value
                                listMen.add(modelMan)
                                indexMan++
                                if (indexMan == countRequest) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.Main) {
                                            adaptorMan.setList(departments.filter { it.id == 1 }
                                                .map { department ->
                                                    DepartmentWithTime(department.id,
                                                        department.salePointName,
                                                        department.address,
                                                        department.latitude,
                                                        department.longitude, "Ваша очередь подойдет через 20 минут")
                                                })
                                            Toast.makeText(
                                                requireContext(),
                                                "Загружено",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                            override fun onMasstransitSummariesError(p0: Error) {}
                        },
                    )
                }
            }
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

}