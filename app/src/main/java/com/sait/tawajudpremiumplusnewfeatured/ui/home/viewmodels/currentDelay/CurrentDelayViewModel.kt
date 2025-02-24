package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.currentDelay

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
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CurrentDelayResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class CurrentDelayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CurrentDelayRepository(CurrentDelayDataSource())

    private val _currentDelayResponse = MutableLiveData<Event<Resource<CurrentDelayResponse>>>()
    val currentDelayResponse: LiveData<Event<Resource<CurrentDelayResponse>>> = _currentDelayResponse

    fun getCurrentDelayData(context: Context, currentDelayRequest: CurrentDelayRequest) = viewModelScope.launch {

        _currentDelayResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getCurrentDelayData(context,currentDelayRequest )
                _currentDelayResponse.postValue(handleResponse(response))
            } else {
/*
                _currentDelayResponse.postValue(
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

                /*    _currentDelayResponse.postValue(
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
                /*    _currentDelayResponse.postValue(
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

                /*    _currentDelayResponse.postValue(
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
                  /*  _currentDelayResponse.postValue(
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

    private fun handleResponse(response: Response<CurrentDelayResponse>): Event<Resource<CurrentDelayResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}