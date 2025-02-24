package com.sait.tawajudpremiumplusnewfeatured.ui.admin.viewmodels.announcements

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
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.admin.models.AdminAnnouncementsResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException





class AdminAnnouncementsViewModel(application: Application) : AndroidViewModel(application) {



    private val repository =
        AdminAnnouncementsRepository(AdminAnnouncementsDataSource())

    private val _adminAnnouncementsResponse = MutableLiveData<Event<Resource<AdminAnnouncementsResponse>>>()
    val adminAnnouncementsResponse: LiveData<Event<Resource<AdminAnnouncementsResponse>>> = _adminAnnouncementsResponse

    fun getAdminAnnouncementsData(context: Context, adminRequest: AdminAnnouncementsRequest) = viewModelScope.launch {

        _adminAnnouncementsResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getAdminAnnouncementsData(context,adminRequest )
                _adminAnnouncementsResponse.postValue(handleResponse(response))
            } else {
                _adminAnnouncementsResponse.postValue(
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
               /*     _adminAnnouncementsResponse.postValue(
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
             /*       _adminAnnouncementsResponse.postValue(
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

    private fun handleResponse(response: Response<AdminAnnouncementsResponse>): Event<Resource<AdminAnnouncementsResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}