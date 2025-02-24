package com.sait.tawajudpremiumplusnewfeatured.ui.summary.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.summary.models.SummaryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.violations.viewmodels.SummaryRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class SummaryViewModel(application: Application) : AndroidViewModel(application) {


    private val repository = SummaryRepository(SummaryDataSource())

    private val _summaryResponse = MutableLiveData<Event<Resource<SummaryResponse>>>()
    val summaryResponse: LiveData<Event<Resource<SummaryResponse>>> = _summaryResponse

    fun getSummaryData(context: Context, summaryRequest: SummaryRequest) = viewModelScope.launch {

        _summaryResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection())
            {
                val response = repository.getSummaryData(context, summaryRequest)
                _summaryResponse.postValue(handleResponse(response))
            } else
            {
                _summaryResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<TawajudApplication>().getString(
                                R.string.no_internet_connection
                            )
                        )
                    )
                )
            }
        }
        catch (t: Throwable)
        {
            when (t) {
                is IOException -> {
                 /*   _summaryResponse.postValue(
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
                 /*   _summaryResponse.postValue(
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

    private fun handleResponse(response: Response<SummaryResponse>): Event<Resource<SummaryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }


}