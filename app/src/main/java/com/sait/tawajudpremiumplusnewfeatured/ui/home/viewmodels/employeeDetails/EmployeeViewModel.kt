package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.employeeDetails

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Event
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmployeeDetailsResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.EmpDetailsRequest
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class EmployeeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EmployeeRepository(EmployeeDataSource())

    private val _employeeResponse = MutableLiveData<Event<Resource<EmployeeDetailsResponse>>>()
    val employeeResponse: LiveData<Event<Resource<EmployeeDetailsResponse>>> = _employeeResponse

    fun getEmployeeData(context: Context, employeeRequest: EmpDetailsRequest) = viewModelScope.launch {

        _employeeResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getEmployeeData(context,employeeRequest )
                _employeeResponse.postValue(handleResponse(response))
            } else {
/*
                _employeeResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<TawajudApplication>().getString(
                                R.string.no_internet_connection
                            )

                        )
                    )
                )
*/
            }
        } catch (t: Throwable) {
            when (t) {

                is SocketTimeoutException -> {
             /*       _employeeResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<TawajudApplication>().getString(
                                    R.string.connection_timeout
                                )
                            )
                        )
                    )*/
                }


                is IOException -> {
              /*      _employeeResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<TawajudApplication>().getString(
                                    R.string.network_failure
                                )
                            )
                        )
                    )*/
                }

                is CancellationException  -> {
                    // Handle coroutine cancellation specifically if needed
                    println("Coroutine was cancelled: ${t.message}")
/*
                    _employeeResponse.postValue(
                        Event(
                            Resource.Error(
                                t.message.toString()
                            )
                        )
                    )*/

                }

                else -> {
                  /*  _employeeResponse.postValue(
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

    private fun handleResponse(response: Response<EmployeeDetailsResponse>): Event<Resource<EmployeeDetailsResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}