package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.viewmodels.Leaves.Approve

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Leaves.Approve_LeavesResponse
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ApprovedHRLeaveViewModel (application: Application) : AndroidViewModel(application) {



    private val repository = ApprovedHRLeavesRepository(ApprovedHRLeaveDataSource())

    private val _approveLeavesResponse = MutableLiveData<Event<Resource<Approve_LeavesResponse>>>()
    val approve_leaves_response: LiveData<Event<Resource<Approve_LeavesResponse>>> = _approveLeavesResponse

    fun getAllApproveLeaves(context: Context, approve_leave_Request : Approve_LeavesRequest) = viewModelScope.launch {

        _approveLeavesResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getApprovedHRLeavesData(context, approve_leave_Request )
                _approveLeavesResponse.postValue(handleResponse(response))
            } else {
                _approveLeavesResponse.postValue(
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
                 /*   _approveLeavesResponse.postValue(
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
                   /* _approveLeavesResponse.postValue(
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

    private fun handleResponse(response: Response<Approve_LeavesResponse>): Event<Resource<Approve_LeavesResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}