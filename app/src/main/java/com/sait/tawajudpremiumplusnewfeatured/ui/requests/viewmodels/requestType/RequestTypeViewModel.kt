package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.requestType

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
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.requestType.RequestTypeResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class RequestTypeViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = RequestTypeRepository(RequestTypeDataSource())

    private val _requestTypeResponse = MutableLiveData<Event<Resource<RequestTypeResponse>>>()
    val requestTypeResponse: LiveData<Event<Resource<RequestTypeResponse>>> = _requestTypeResponse

    fun getRequestTypeData(context: Context, commonRequest: CommonRequest) = viewModelScope.launch {

        _requestTypeResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getRequestTypeData(context,commonRequest )
                _requestTypeResponse.postValue(handleResponse(response))
            } else {
                _requestTypeResponse.postValue(
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
                 /*   _requestTypeResponse.postValue(
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
                /*    _requestTypeResponse.postValue(
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

    private fun handleResponse(response: Response<RequestTypeResponse>): Event<Resource<RequestTypeResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}