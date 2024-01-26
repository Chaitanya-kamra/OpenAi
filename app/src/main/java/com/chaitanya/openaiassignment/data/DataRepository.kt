package com.chaitanya.openaiassignment.data

import com.chaitanya.openaiassignment.data.api.ApiService
import com.chaitanya.openaiassignment.domain.model.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getDashboardData(): Response<ApiResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getDashboard()
        }
    }
}
