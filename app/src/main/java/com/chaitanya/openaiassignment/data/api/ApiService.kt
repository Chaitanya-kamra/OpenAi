package com.chaitanya.openaiassignment.data.api

import com.chaitanya.openaiassignment.domain.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("api/v1/dashboardNew")
    suspend fun getDashboard(): Response<ApiResponse>
}
