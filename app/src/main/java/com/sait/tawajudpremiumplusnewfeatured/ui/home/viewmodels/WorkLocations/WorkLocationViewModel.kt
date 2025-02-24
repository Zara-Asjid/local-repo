package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.WorkLocations

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
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.WorkLocationResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class WorkLocationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WorkLocationRepository(WorkLocationDataSource())

    private val _workLocationResponse = MutableLiveData<Event<Resource<WorkLocationResponse>>>()
    val workLocationResponse: LiveData<Event<Resource<WorkLocationResponse>>> = _workLocationResponse

    fun getWorkLocationData(context: Context, currentDelayRequest: WorkLocationRequest) = viewModelScope.launch {

        _workLocationResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getWorkLocationData(context,currentDelayRequest )
                _workLocationResponse.postValue(handleResponse(response))
            } else {


/*
                _workLocationResponse.postValue(
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

         /*           _workLocationResponse.postValue(
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
              /*      _workLocationResponse.postValue(
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

              /*      _workLocationResponse.postValue(
                        Event(
                            Resource.Error(
                                t.message.toString()
                            )
                        )
                    )*/

                }

                else -> {
              /*      _workLocationResponse.postValue(
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

    private fun handleResponse(response: Response<WorkLocationResponse>): Event<Resource<WorkLocationResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}