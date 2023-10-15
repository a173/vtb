package com.example.vtb

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.vtb.databinding.ActivityMainBinding
import com.example.vtb.enums.ServiceType
import com.example.vtb.interfaces.DepartmentInterface
import com.example.vtb.interfaces.ExpressInterface
import com.example.vtb.interfaces.PointInterface
import com.example.vtb.interfaces.ServiceInterface
import com.example.vtb.models.Department
import com.example.vtb.retrofit.RetrofitAPI
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), ExpressInterface, DepartmentInterface, PointInterface, ServiceInterface {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    private var express = false
    private var point = Point()
    private var service = ServiceType.CARDS
    private lateinit var departments: List<Department>
    private lateinit var retrofit: Retrofit
    lateinit var api: RetrofitAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("ca517243-2e5b-450e-bf73-d59f5d0ae905")
        requestLocalPermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.nav_host_auth)

        retrofit = Retrofit.Builder()
            .baseUrl("https://d132-176-52-101-65.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(RetrofitAPI::class.java)
        CoroutineScope(Dispatchers.IO).launch {
//            departments = api.getDepartment("Казань").offices
            departments = listOf(
                Department(1, "участок №1", "Павлова, 23", 55.785, 49.1178),
                Department(2, "участок №2", "Калинина, 33", 55.808189, 49.054722),
                Department(3, "участок №3", "Кутузовский, 48", 55.786942, 49.178520)
            )
        }
    }

    private fun requestLocalPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 0
            )
            return
        }
    }

    override fun setExpress(parameter: Boolean) {
        express = parameter
    }

    override fun getExpress(): Boolean = express
    override fun getDepartments(): List<Department> = departments
    override fun setPoint(parameter: Point) {
        point = parameter
    }
    override fun getPoint(): Point = point
    override fun getService(): ServiceType = service

    override fun setService(parameter: ServiceType) {
        service = parameter
    }

}