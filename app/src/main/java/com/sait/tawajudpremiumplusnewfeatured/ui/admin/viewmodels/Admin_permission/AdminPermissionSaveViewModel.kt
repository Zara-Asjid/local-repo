package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.Admin_permission

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
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.permission.RequestSavePermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class AdminPermissionSaveViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = AdminPermissionSaveRepository(AdminRequestSavePermissionDataSource())

    private val _requestSavePermissionResponse = MutableLiveData<Event<Resource<RequestSavePermissionResponse>>>()
    val requestSavePermissionResponse: LiveData<Event<Resource<RequestSavePermissionResponse>>> =_requestSavePermissionResponse

    fun getRequestSavePermissionData(
        mContext: Context?,
        requestSaveLeaveRequest: RequestSavePermissionRequest
    ) = viewModelScope.launch {

        _requestSavePermissionResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getRequestSavePermissionData(mContext!!,requestSaveLeaveRequest)
                _requestSavePermissionResponse.postValue(handleResponse(response))
            } else {
                _requestSavePermissionResponse.postValue(
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
                /*    _requestSavePermissionResponse.postValue(
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
                /*    _requestSavePermissionResponse.postValue(
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
             /*       _requestSavePermissionResponse.postValue(
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

    private fun handleResponse(response: Response<RequestSavePermissionResponse>): Event<Resource<RequestSavePermissionResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}