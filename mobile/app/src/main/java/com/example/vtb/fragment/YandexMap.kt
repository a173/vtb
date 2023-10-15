package com.example.vtb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.vtb.MainActivity
import com.example.vtb.R
import com.example.vtb.adapters.DataAdapter
import com.example.vtb.databinding.FragmentYandexMapBinding
import com.example.vtb.interfaces.DepartmentInterface
import com.example.vtb.interfaces.PointInterface
import com.example.vtb.models.DepartmentWithTime
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.Session.RouteListener
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

val START_POINT = Point(55.785, 49.1178)
val DEFAULT_POINT = Point(55.7828, 49.1261)

class YandexMap : Fragment() {

    lateinit var binding: FragmentYandexMapBinding
    private lateinit var drivingRouter: DrivingRouter
    private lateinit var pedestrianRouter: PedestrianRouter

    private var drivingSession: DrivingSession? = null
    private var pedestrianSession: Session? = null

    lateinit var routesCollection: MapObjectCollection
    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            routesCollection.addPolyline(drivingRoutes.first().geometry)
        }
        override fun onDrivingRoutesError(p0: Error) {}
    }

    private val pedestrianRouteListener = object : RouteListener {
        override fun onMasstransitRoutes(p0: MutableList<Route>) {
            routesCollection.addPolyline(p0.first().geometry)
        }
        override fun onMasstransitRoutesError(p0: Error) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        MapKitFactory.initialize(requireContext())
        binding = FragmentYandexMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnClose.setOnClickListener {
            binding.info.isGone = true
        }
        binding.btnWrite.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_yandexMap_to_talonFragment)
        }
        binding.time.adapter = DataAdapter(listOf("10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45"))
        binding.map.map.mapObjects.clear()
        binding.map.map.move(
            CameraPosition(Point(55.785, 49.1178), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 4f), null)
        val mapKit = MapKitFactory.getInstance()
        val yourLocation = mapKit.createUserLocationLayer(binding.map.mapWindow)
        yourLocation.isVisible = true
        if (arguments?.getBoolean("one point") == true) {
            val department = arguments?.getSerializable("department") as DepartmentWithTime
            val pointDepartment = Point(department.latitude!!, department.longitude!!)
            binding.map.map.mapObjects.addPlacemark(pointDepartment,
                ImageProvider.fromResource(requireContext(), R.drawable.target))
            drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
            pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter()
            routesCollection = binding.map.map.mapObjects
            createRoute(listOf((activity as PointInterface).getPoint(), pointDepartment))
        } else {
            val departments = (activity as DepartmentInterface).getDepartments()
            for (department in departments) {
                val vtb = binding.map.map.mapObjects.addPlacemark(Point(department.latitude!!, department.longitude!!),
                    ImageProvider.fromResource(requireContext(), R.drawable.target))
                vtb.addTapListener(object : MapObjectTapListener {
                    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
                        binding.info.isVisible = true
                        binding.address.text = department.address
                        return true
                    }
                })
            }
        }


    }

    private fun createRoute(points: List<Point>) {
        val requestPoints = buildList {
            add(RequestPoint(points.first(), RequestPointType.WAYPOINT, null))
            add(RequestPoint(points.last(), RequestPointType.WAYPOINT, null))
        }
        if (arguments?.getBoolean("isMan") == true) {
            pedestrianSession = pedestrianRouter.requestRoutes(
                requestPoints,
                TimeOptions(),
                pedestrianRouteListener,
            )
        } else {
            drivingSession = drivingRouter.requestRoutes(
                requestPoints,
                DrivingOptions(),
                VehicleOptions(),
                drivingRouteListener,
            )
        }
    }

    override fun onStop() {
        binding.map.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        binding.map.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

}