package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.manager.models.AdminResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class AdminViewModel(application: Application) : AndroidViewModel(application) {



    private val repository =
       AdminRepository(AdminDataSource())

    private val _adminResponse = MutableLiveData<Event<Resource<AdminResponse>>>()
    val adminResponse: LiveData<Event<Resource<AdminResponse>>> = _adminResponse

    fun getAdminData(context: Context, adminRequest: AdminRequest) = viewModelScope.launch {

        _adminResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getAdminData(context,adminRequest )
                _adminResponse.postValue(handleResponse(response))
            } else {
                _adminResponse.postValue(
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
            Log.e("exception_check",t.message.toString())

            when (t) {

                        is SocketTimeoutException -> {
              /*      _adminResponse.postValue(
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
             /*       _adminResponse.postValue(
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

              /*      _adminResponse.postValue(
                        Event(
                            Resource.Error(
                                t.message.toString()
                            )
                        )
                    )*/

                }

                else -> {
               /*     _adminResponse.postValue(
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

    private fun handleResponse(response: Response<AdminResponse>): Event<Resource<AdminResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}