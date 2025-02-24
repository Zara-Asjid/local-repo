package com.sait.tawajudpremiumplusnewfeatured.ui.pdf.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.pdf.models.PdfReportResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class PdfReportViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = PdfReportRepository(PdfReportDataSource())

    private val _pdfReportResponse = MutableLiveData<Event<Resource<PdfReportResponse>>>()
    val pdfReportResponse: LiveData<Event<Resource<PdfReportResponse>>> = _pdfReportResponse

    fun getpdfReportResponseData(
        mContext: Context, pdfReportRequest: PdfReportRequest) = viewModelScope.launch {

        _pdfReportResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getPdfReportData(mContext,pdfReportRequest)
                _pdfReportResponse.postValue(handleResponse(response))
            } else {
                _pdfReportResponse.postValue(
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
                /*    _pdfReportResponse.postValue(
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
                /*    _pdfReportResponse.postValue(
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

    private fun handleResponse(response: Response<PdfReportResponse>): Event<Resource<PdfReportResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }




}