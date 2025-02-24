package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.RequestResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class SelfRequestViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = SelfRequestRepository(SelfRequestDataSource())

    private val _requestResponse = MutableLiveData<Event<Resource<RequestResponse>>>()
    val requestResponse: LiveData<Event<Resource<RequestResponse>>> = _requestResponse

    fun getRequestData(context: Context, employeeId: Int, lang: String,type: String) = viewModelScope.launch {

        _requestResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getRequestData(context,employeeId,lang,type )
                _requestResponse.postValue(handleResponse(response))
            } else {
                _requestResponse.postValue(
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
                 /*   _requestResponse.postValue(
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
               /*     _requestResponse.postValue(
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

    private fun handleResponse(response: Response<RequestResponse>): Event<Resource<RequestResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}