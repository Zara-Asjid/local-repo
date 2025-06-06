package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.Pending.PendingHRPermissionDataSource
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.hr.viewmodels_hr.Permisssions.Pending.PendingHRPermissionRepository
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingHRPermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissonRequest
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PendingHRPermissionViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = PendingHRPermissionRepository(PendingHRPermissionDataSource())

    private val _pendingPermissionResponse = MutableLiveData<Event<Resource<PendingHRPermissionResponse>>>()
    val pending_permission_response: LiveData<Event<Resource<PendingHRPermissionResponse>>> = _pendingPermissionResponse

    fun getPendingHRPermissionStatus(context: Context, pending_permission_request : PendingPermissonRequest) = viewModelScope.launch {

        _pendingPermissionResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getPendingHRPermissionData(context, pending_permission_request )
                _pendingPermissionResponse.postValue(handleResponse(response))
            } else {
                _pendingPermissionResponse.postValue(
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
                    /*_pendingPermissionResponse.postValue(
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
                   /* _pendingPermissionResponse.postValue(
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

    private fun handleResponse(response: Response<PendingHRPermissionResponse>): Event<Resource<PendingHRPermissionResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}