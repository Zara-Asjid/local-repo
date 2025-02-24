package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.ManualEntry

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingHRManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.PendingManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PendingHRManualEntryViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = PendingHRManualEntryRepository(PendingHRManualEntryDataSource())

    private val _pendingManualEntryResponse = MutableLiveData<Event<Resource<PendingHRManualEntryResponse>>>()
    val pending_manual_entry_response: LiveData<Event<Resource<PendingHRManualEntryResponse>>> = _pendingManualEntryResponse

    fun getPendingManualEntryStatus(context: Context, pending_manual_entry_request : PendingManualEntryRequest) = viewModelScope.launch {

        _pendingManualEntryResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getPendingHRManualEntryData(context, pending_manual_entry_request )
                _pendingManualEntryResponse.postValue(handleResponse(response))
            } else {
                _pendingManualEntryResponse.postValue(
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
                   /* _pendingManualEntryResponse.postValue(
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
                  /*  _pendingManualEntryResponse.postValue(
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

    private fun handleResponse(response: Response<PendingHRManualEntryResponse>): Event<Resource<PendingHRManualEntryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}