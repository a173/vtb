package com.example.vtb.retrofit

import com.example.vtb.models.ListDepartments
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("/office")
    suspend fun getDepartment(@Query("city") city: String): ListDepartments

}