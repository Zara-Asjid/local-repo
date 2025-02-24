package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Reject

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickReject.onClickRejectManualEntryRequest

import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class onClickRejectManualEntryHRViewModel (application: Application) : AndroidViewModel(application) {

    private val repository =
        onClickRejectManualEntryHRRepository(onClickRejectManualEntryHRDataSource())

    private val _onCLickrejectManualEntryResponse =
        MutableLiveData<Event<Resource<onClickApproveManualEntryResponse>>>()

    val onclick_reject_manual_entry_response: LiveData<Event<Resource<onClickApproveManualEntryResponse>>> =
        _onCLickrejectManualEntryResponse

    fun getonClickRejectManualEntryStatus(
        context: Context,
        reject_manual_entry_request: onClickRejectManualEntryRequest
    ) = viewModelScope.launch {

        _onCLickrejectManualEntryResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response =
                    repository.getonclickrejectManualEntryHRData(context, reject_manual_entry_request)
                _onCLickrejectManualEntryResponse.postValue(handleResponse(response))
            } else {
                _onCLickrejectManualEntryResponse.postValue(
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
                 /*   _onCLickrejectManualEntryResponse.postValue(
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
                    _onCLickrejectManualEntryResponse.postValue(
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

    private fun handleResponse(response: Response<onClickApproveManualEntryResponse>): Event<Resource<onClickApproveManualEntryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }
}




