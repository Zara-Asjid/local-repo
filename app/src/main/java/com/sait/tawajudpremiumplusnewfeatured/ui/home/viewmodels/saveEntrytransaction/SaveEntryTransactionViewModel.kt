package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.saveEntrytransaction

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
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.SaveManualEntryResponse
import com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction.SaveEntryTransactionRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class SaveEntryTransactionViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = SaveEntryTransactionRepository(SaveEntryTransactionDataSource())

    private val _saveEntrytransactionResponse = MutableLiveData<Event<Resource<SaveManualEntryResponse>>>()
    val saveEntrytransactionResponse: LiveData<Event<Resource<SaveManualEntryResponse>>> = _saveEntrytransactionResponse

    fun getSaveEntryTransactionData(context: Context, saveEntrytransactionRequest: SaveManualEntryRequest) = viewModelScope.launch {

        _saveEntrytransactionResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getSaveEntryTransactionData(context,saveEntrytransactionRequest )
                _saveEntrytransactionResponse.postValue(handleResponse(response))
            } else {
                _saveEntrytransactionResponse.postValue(
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
                /*    _saveEntrytransactionResponse.postValue(
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
                 /*   _saveEntrytransactionResponse.postValue(
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

    private fun handleResponse(response: Response<SaveManualEntryResponse>): Event<Resource<SaveManualEntryResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}