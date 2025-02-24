package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.ManualEntry.Approve

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.ManualEntry.onClickApprove.onClickApproveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class onClickApproveManualEntryHRViewModel (application: Application) : AndroidViewModel(application) {

    private val repository = onClickApproveManualEntryHRRepository(onClickApproveManualEntryHRDataSource())

    private val _onCLickapproveManualEntryResponse = MutableLiveData<Event<Resource<onClickApproveManualEntryResponse>>>()

    val onclick_approve_manual_entry_response: LiveData<Event<Resource<onClickApproveManualEntryResponse>>> = _onCLickapproveManualEntryResponse

    fun getonClickApproveManualEntryStatus(context: Context, approve_manual_entry_request : onClickApproveManualEntryRequest) = viewModelScope.launch {

        _onCLickapproveManualEntryResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getonclickapproveManualEntryHRData(context, approve_manual_entry_request )
                _onCLickapproveManualEntryResponse.postValue(handleResponse(response))
            } else {
                _onCLickapproveManualEntryResponse.postValue(
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
                 /*   _onCLickapproveManualEntryResponse.postValue(
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
                  /*  _onCLickapproveManualEntryResponse.postValue(
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

    private fun handleResponse(response: Response<onClickApproveManualEntryResponse>): Event<Resource<onClickApproveManualEntryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}