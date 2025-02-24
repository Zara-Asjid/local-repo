package com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.dashboard.models.DashboardResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException


class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DashboardRepository(DashboardDataSource())

    private val _dashboardResponse = MutableLiveData<Event<Resource<DashboardResponse>>>()
    val dashboardResponse: LiveData<Event<Resource<DashboardResponse>>> = _dashboardResponse

    fun getDashboardData(context: Context, dashboardRequest: DashboardRequest) = viewModelScope.launch {

        _dashboardResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getDashboardData(context,dashboardRequest )
                //Log.d("responseData", "getDashboardData: "+employeeId +" "+ lang)
                _dashboardResponse.postValue(handleResponse(response))
            } else {
                _dashboardResponse.postValue(
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
               /*     _dashboardResponse.postValue(
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
               /*     _dashboardResponse.postValue(
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

               /*     _dashboardResponse.postValue(
                        Event(
                            Resource.Error(
                                t.message.toString()
                            )
                        )
                    )*/

                }


                else -> {
               /*     _dashboardResponse.postValue(
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

    private fun handleResponse(response: Response<DashboardResponse>): Event<Resource<DashboardResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}