package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.ApprovedList

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.ApprovePermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class ApprovedHRPermissionViewModel (application: Application) : AndroidViewModel(application) {



    private val repository = ApprovedHRPermissionRepository(ApprovedHRPermissionDataSource())

    private val _approveLeavesResponse = MutableLiveData<Event<Resource<ApprovePermissionResponse>>>()
    val approved_permission_response: LiveData<Event<Resource<ApprovePermissionResponse>>> = _approveLeavesResponse

    fun getHRApprovedPermission(context: Context, approve_leave_Request : ApprovePermissionRequest) = viewModelScope.launch {

        _approveLeavesResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getApprovedHRPermissionData(context, approve_leave_Request )
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
                  /*  _approveLeavesResponse.postValue(
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
                  /*  _approveLeavesResponse.postValue(
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

    private fun handleResponse(response: Response<ApprovePermissionResponse>): Event<Resource<ApprovePermissionResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}