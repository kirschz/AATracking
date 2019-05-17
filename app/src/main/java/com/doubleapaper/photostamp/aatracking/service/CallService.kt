package com.doubleapaper.photostamp.aatracking.service

import com.doubleapaper.photostamp.aatracking.dao.SaveMobile
import com.doubleapaper.photostamp.aatracking.dao.SaveTracking
import com.doubleapaper.photostamp.aatracking.dao.ServiceResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class CallService {
    private val URL = ""
    internal var retrofit: Retrofit

    constructor() {
        this.retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
    }

    interface SaveTrackingDataService {
        @POST("SaveTrackingData")
        fun SaveTrackingData(@Body saveTracking: SaveTracking): Call<ServiceResponse>
    }
    interface SaveMobileDataService {
        @POST("SaveMobileData")
        fun SaveMobileData(@Body saveMobile: SaveMobile): Call<ServiceResponse>
    }
}