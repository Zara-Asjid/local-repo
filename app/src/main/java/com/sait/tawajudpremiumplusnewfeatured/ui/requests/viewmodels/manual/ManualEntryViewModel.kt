package com.sait.tawajudpremiumplusnewfeatured.ui.requests.viewmodels.manual

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
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.requests.models.manual.ManualEntryResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class ManualEntryViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = ManualEntryRepository(ManualEntryDataSource())

    private val _manualEntryResponse = MutableLiveData<Event<Resource<ManualEntryResponse>>>()
    val manualEntryResponse: LiveData<Event<Resource<ManualEntryResponse>>> = _manualEntryResponse

    fun getManualEntryData(context: Context, manualEntryRequest: ManualEntryRequest) = viewModelScope.launch {

        _manualEntryResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getManualEntryData(context,manualEntryRequest )
                _manualEntryResponse.postValue(handleResponse(response))
            } else {
                _manualEntryResponse.postValue(
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
                 /*   _manualEntryResponse.postValue(
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
                /*    _manualEntryResponse.postValue(
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

    private fun handleResponse(response: Response<ManualEntryResponse>): Event<Resource<ManualEntryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}