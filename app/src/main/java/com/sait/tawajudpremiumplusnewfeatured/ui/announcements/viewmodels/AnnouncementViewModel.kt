package com.sait.tawajudpremiumplusnewfeatured.ui.announcements.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.announcements.models.AnnouncementResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class AnnouncementViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = AnnouncementRepository(AnnouncementDataSource())

    private val _announcementResponse = MutableLiveData<Event<Resource<AnnouncementResponse>>>()
    val announcementResponse: LiveData<Event<Resource<AnnouncementResponse>>> = _announcementResponse

    fun getAnnouncementData(context: Context,  announcementRequest: AnnouncementRequest) = viewModelScope.launch {

        _announcementResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                Log.e("commonRequest",announcementRequest.toString())

                val response = repository.getAnnouncementData(context,announcementRequest)
                _announcementResponse.postValue(handleResponse(response))
            } else {
                _announcementResponse.postValue(
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
                   /* _announcementResponse.postValue(
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
                  /*  _announcementResponse.postValue(
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

    private fun handleResponse(response: Response<AnnouncementResponse>): Event<Resource<AnnouncementResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}