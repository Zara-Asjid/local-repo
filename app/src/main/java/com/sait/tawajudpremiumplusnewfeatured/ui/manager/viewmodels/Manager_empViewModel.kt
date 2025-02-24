package com.sait.tawajudpremiumplusnewfeatured.ui.manager.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Event
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.ManagerEmpResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class Manager_empViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = Manager_empRepository(Manager_empDataSource())

    private val _manager_empResponse = MutableLiveData<Event<Resource<ManagerEmpResponse>>>()
    val manager_empResponse: LiveData<Event<Resource<ManagerEmpResponse>>> = _manager_empResponse

    fun getManager_empData(context: Context,  managerEmpRequest: ManagerEmpRequest) = viewModelScope.launch {

        _manager_empResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                Log.e("commonRequest",managerEmpRequest.toString())

                val response = repository.getManager_empData(context,managerEmpRequest)
                _manager_empResponse.postValue(handleResponse(response))
            } else {
                _manager_empResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<TawajudApplication>().getString(
                                R.string.no_internet_connection
                            )
                        )
                    )
                )
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                   /* _manager_empResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<TawajudApplication>().getString(
                                    R.string.network_failure
                                )
                            )
                        )
                    )*/
                }
                else -> {
                /*    _manager_empResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<TawajudApplication>().getString(
                                    R.string.conversion_error
                                )
                            )
                        )
                    )*/
                }
            }
        }
    }

    private fun handleResponse(response: Response<ManagerEmpResponse>): Event<Resource<ManagerEmpResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}