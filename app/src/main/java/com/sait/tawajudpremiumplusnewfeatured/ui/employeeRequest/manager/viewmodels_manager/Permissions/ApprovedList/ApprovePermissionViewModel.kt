package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.ApprovedList

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

class ApprovePermissionViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = ApprovePermissionRepository(ApprovePermissionDataSource())

    private val _approvePermissionResponse = MutableLiveData<Event<Resource<ApprovePermissionResponse>>>()
    val approve_permission_response: LiveData<Event<Resource<ApprovePermissionResponse>>> = _approvePermissionResponse

    fun getApprovePermissionStatus(context: Context, approve_permission_request : ApprovePermissionRequest) = viewModelScope.launch {

        _approvePermissionResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getApprovePermissionData(context, approve_permission_request )
                _approvePermissionResponse.postValue(handleResponse(response))
            } else {
                _approvePermissionResponse.postValue(
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
                  /*  _approvePermissionResponse.postValue(
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
                /*    _approvePermissionResponse.postValue(
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