package com.sait.tawajudpremiumplusnewfeatured.ui.login.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Event
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.LoginResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NoficationTypesResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.login.models.NotificationRequest
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class NotificationViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = NotificationRepository(NotificationDataSource())

    private val _notificationResponse = MutableLiveData<Event<Resource<NoficationTypesResponse>>>()
    val notificationResponse: LiveData<Event<Resource<NoficationTypesResponse>>> = _notificationResponse

    fun getNotificationTypeData(context: Context,notificationRequest: NotificationRequest) = viewModelScope.launch {

        _notificationResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getNotificationData(context,notificationRequest)
                _notificationResponse.postValue(handleResponse(response))
            } else {
                _notificationResponse.postValue(
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
              /*      _notificationResponse.postValue(
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
                /*    _notificationResponse.postValue(
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

            /*        _notificationResponse.postValue(
                        Event(
                            Resource.Error(
                                t.message.toString()
                            )
                        )
                    )*/

                }
                else -> {
              /*      _notificationResponse.postValue(
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

    private fun handleResponse(response: Response<NoficationTypesResponse>): Event<Resource<NoficationTypesResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}