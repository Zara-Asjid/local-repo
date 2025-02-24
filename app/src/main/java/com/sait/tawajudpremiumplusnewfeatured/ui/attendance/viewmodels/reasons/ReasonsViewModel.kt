package com.sait.tawajudpremiumplusnewfeatured.ui.attendance.viewmodels.reasons

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
import com.sait.tawajudpremiumplusnewfeatured.ui.attendance.models.ReasonsResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class ReasonsViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = ReasonRepository(ReasonsDataSource())

    private val _reasonsResponse = MutableLiveData<Event<Resource<ReasonsResponse>>>()
    val reasonsResponse: LiveData<Event<Resource<ReasonsResponse>>> = _reasonsResponse

    fun getReasonsData(context: Context) = viewModelScope.launch {

        _reasonsResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getReasonsData(context )
                _reasonsResponse.postValue(handleResponse(response))
            } else {
                _reasonsResponse.postValue(
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
                  /*  _reasonsResponse.postValue(
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
                    _reasonsResponse.postValue(
                        Event(
                            Resource.Error(
                                getApplication<TawajudApplication>().getString(
                                    R.string.conversion_error
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    private fun handleResponse(response: Response<ReasonsResponse>): Event<Resource<ReasonsResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}