package com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.models.ViolationResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class ViolationViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = ViolationRepository(ViolationDataSource())

    private val _violationResponse = MutableLiveData<Event<Resource<ViolationResponse>>>()
    val violationResponse: LiveData<Event<Resource<ViolationResponse>>> = _violationResponse

    fun getViolationData(context: Context, employeeId: Int, from_date: String, to_date: String, lang: String) = viewModelScope.launch {

        _violationResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getViolationData(context,employeeId,from_date,to_date,lang )
                _violationResponse.postValue(handleResponse(response))
            } else {
                _violationResponse.postValue(
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

                is SocketTimeoutException -> {
               /*     _violationResponse.postValue(
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
                /*    _violationResponse.postValue(
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

                    _violationResponse.postValue(
                        Event(
                            Resource.Error(
                                t.message.toString()
                            )
                        )
                    )

                }
                else -> {
            /*        _violationResponse.postValue(
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

    private fun handleResponse(response: Response<ViolationResponse>): Event<Resource<ViolationResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}