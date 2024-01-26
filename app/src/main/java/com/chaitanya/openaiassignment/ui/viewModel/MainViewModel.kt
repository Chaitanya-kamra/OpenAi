package com.chaitanya.openaiassignment.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaitanya.openaiassignment.data.DataRepository
import com.chaitanya.openaiassignment.domain.model.ApiResponse
import com.chaitanya.openaiassignment.utils.DashboardLinkTabState
import com.chaitanya.openaiassignment.utils.DataHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataRepository: DataRepository) : ViewModel() {

    private val _dashboardData = MutableLiveData<DataHandler<ApiResponse>>()
    val dashboardData: LiveData<DataHandler<ApiResponse>> get() = _dashboardData

    fun fetchData() {
        viewModelScope.launch {

            _dashboardData.postValue(DataHandler.LOADING())
            try {
                val response =dataRepository.getDashboardData()
                if (response.isSuccessful){
                    response.body()?.let {
                        println(it)
                        _dashboardData.postValue(DataHandler.SUCCESS(it))
                    }
                }else{
                    Log.e("Error",response.errorBody().toString())
                    _dashboardData.postValue(DataHandler.ERROR(message = "Unable To Get Data"))
                }

            }catch (e:Exception){
                Log.e("Error",e.printStackTrace().toString())
                _dashboardData.postValue(DataHandler.ERROR(message = e.message.toString()))
            }
        }
    }

    var dashboardLinkTabState = DashboardLinkTabState.TOP

    fun changeDashboardLinkTabState(state: DashboardLinkTabState){
        dashboardLinkTabState = state
    }

}
