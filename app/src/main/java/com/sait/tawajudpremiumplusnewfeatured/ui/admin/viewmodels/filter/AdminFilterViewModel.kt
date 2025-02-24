package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.filter

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import com.sait.tawajudpremiumplusnewfeatured.R
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Event
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminFilterResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException





class AdminFilterViewModel(application: Application) : AndroidViewModel(application) {



    private val repository =
        AdminFilterRepository(AdminFilterDataSource())

    private val _adminFilterResponse = MutableLiveData<Event<Resource<AdminFilterResponse>>>()
    val adminFilterResponse: LiveData<Event<Resource<AdminFilterResponse>>> = _adminFilterResponse

    fun getAdminFilterData(context: Context, adminRequest: AdminFilterRequest) = viewModelScope.launch {

        _adminFilterResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getAdminFilterData(context,adminRequest )
                _adminFilterResponse.postValue(handleResponse(response))
            } else {
                _adminFilterResponse.postValue(
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
            Log.e("admin",t.toString())
            when (t) {
                is IOException -> {
                  /*  _adminFilterResponse.postValue(
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
                 /*   _adminFilterResponse.postValue(
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

    private fun handleResponse(response: Response<AdminFilterResponse>): Event<Resource<AdminFilterResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}