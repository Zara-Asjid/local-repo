package com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.manager.viewmodels_manager.Permissions.Pending

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
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissionResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.employeeRequest.models.Permission.PendingPermissonRequest
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class PendingPermissionViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = PendingPermissionRepository(PendingPermissionDataSource())

    private val _pendingPermissionResponse = MutableLiveData<Event<Resource<PendingPermissionResponse>>>()
    val pending_permission_response: LiveData<Event<Resource<PendingPermissionResponse>>> = _pendingPermissionResponse

    fun getPendingPermissionStatus(context: Context, pending_permission_request : PendingPermissonRequest) = viewModelScope.launch {

        _pendingPermissionResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getPendingPermissionData(context, pending_permission_request )
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
                  /*  _pendingPermissionResponse.postValue(
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
                  /*  _pendingPermissionResponse.postValue(
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

    private fun handleResponse(response: Response<PendingPermissionResponse>): Event<Resource<PendingPermissionResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}