package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.leave

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
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.leave.RequestSaveLeaveResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class RequestSaveLeaveViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = RequestSaveLeaveRepository(RequestSaveLeaveDataSource())

    private val _requestSaveLeaveResponse = MutableLiveData<Event<Resource<RequestSaveLeaveResponse>>>()
    val requestSaveLeaveResponse: LiveData<Event<Resource<RequestSaveLeaveResponse>>> = _requestSaveLeaveResponse

    fun getRequestSaveLeaveData(
        mContext: Context?,
        requestSaveLeaveRequest: RequestSaveLeaveRequest
    ) = viewModelScope.launch {

        _requestSaveLeaveResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getRequestSaveLeaveData(mContext!!,requestSaveLeaveRequest)
                _requestSaveLeaveResponse.postValue(handleResponse(response))
            } else {
                _requestSaveLeaveResponse.postValue(
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
                /*    _requestSaveLeaveResponse.postValue(
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
                 /*   _requestSaveLeaveResponse.postValue(
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

    private fun handleResponse(response: Response<RequestSaveLeaveResponse>): Event<Resource<RequestSaveLeaveResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}