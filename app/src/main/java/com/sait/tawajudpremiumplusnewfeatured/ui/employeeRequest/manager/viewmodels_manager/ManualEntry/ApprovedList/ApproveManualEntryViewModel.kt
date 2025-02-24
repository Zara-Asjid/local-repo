package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.ApprovedList

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.ApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ApproveManualEntryViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = ApproveManualEntryRepository(ApproveManualEntryDataSource())

    private val _approveManualEntryResponse = MutableLiveData<Event<Resource<ApproveManualEntryResponse>>>()
    val approve_manual_entry_response: LiveData<Event<Resource<ApproveManualEntryResponse>>> = _approveManualEntryResponse

    fun getApproveManualEntryStatus(context: Context, approve_manual_entry_request : ApproveManualEntryRequest) = viewModelScope.launch {

        _approveManualEntryResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getApproveManualEntryHRData(context, approve_manual_entry_request )
                _approveManualEntryResponse.postValue(handleResponse(response))
            } else {
                _approveManualEntryResponse.postValue(
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
                /*    _approveManualEntryResponse.postValue(
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
                /*    _approveManualEntryResponse.postValue(
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

    private fun handleResponse(response: Response<ApproveManualEntryResponse>): Event<Resource<ApproveManualEntryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}