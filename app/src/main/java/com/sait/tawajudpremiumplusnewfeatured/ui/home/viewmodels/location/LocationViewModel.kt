package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.location

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
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.LocationResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class LocationViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = LocationRepository(LocationDataSource())

    private val _locationResponse = MutableLiveData<Event<Resource<LocationResponse>>>()
    val locationResponse: LiveData<Event<Resource<LocationResponse>>> = _locationResponse

    fun getLocationData(context: Context, locationRequest: CommonRequest) = viewModelScope.launch {

        _locationResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getLocationData(context,locationRequest )
                _locationResponse.postValue(handleResponse(response))
            }
            else {
/*
                _locationResponse.postValue(
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
             /*       _locationResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<TawajudApplication>().getString(
                                    R.string.network_failure
                                )
                            )
                        )
                    )*/
                }

                is IOException -> {
                /*    _locationResponse.postValue(
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
                 //   println("Coroutine was cancelled: ${t.message}")

               /*     _locationResponse.postValue(
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
               /*     _locationResponse.postValue(
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

    private fun handleResponse(response: Response<LocationResponse>):
            Event<Resource<LocationResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}