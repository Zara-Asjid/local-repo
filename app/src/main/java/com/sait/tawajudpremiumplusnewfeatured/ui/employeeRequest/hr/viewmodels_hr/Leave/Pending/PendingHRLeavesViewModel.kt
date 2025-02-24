package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Leave.Pending

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingForManagerRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.PendingHRLeavesResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class PendingHRLeavesViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = PendingHRLeavesRepository(PendingHRLeavesDataSource())

    private val _pendingHRLeavesResponse = MutableLiveData<Event<Resource<PendingHRLeavesResponse>>>()
    val pendingHR_leaves_response: LiveData<Event<Resource<PendingHRLeavesResponse>>> = _pendingHRLeavesResponse

     fun getPendingHRLeaves(context: Context, pending_leave_Request : PendingForManagerRequest) = viewModelScope.launch {

         _pendingHRLeavesResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getPendingHRLeavesData(context, pending_leave_Request )
                _pendingHRLeavesResponse.postValue(handleResponse(response))
            } else {
                _pendingHRLeavesResponse.postValue(
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
                 /*   _pendingHRLeavesResponse.postValue(
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
                  /*  _pendingHRLeavesResponse.postValue(
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

    private fun handleResponse(response: Response<PendingHRLeavesResponse>): Event<Resource<PendingHRLeavesResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))

            }
        }
        return Event(Resource.Error(response.message()))
    }





}