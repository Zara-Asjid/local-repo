package com.sait.tawajudpremiumplusnewfeatured.ui.home.viewmodels.transaction_temp

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
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.CommonRequest
import com.sait.tawajudpremiumplusnewfeatured.ui.home.models.TransactionResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException



class TransactionTempViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = TransactionTempRepository(TransactionTempDataSource())

    private val _transactionResponse = MutableLiveData<Event<Resource<TransactionResponse>>>()
    val transactionResponse: LiveData<Event<Resource<TransactionResponse>>> = _transactionResponse

    fun getTransactionData(context: Context, transactionRequest: CommonRequest) = viewModelScope.launch {

        _transactionResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getTransactionTempData(context,transactionRequest )
                _transactionResponse.postValue(handleResponse(response))
            } else {
/*
                _transactionResponse.postValue(
                    Event(
                        Resource.Error(
                            getApplication<TawajudApplication>().getString(
                                R.string.no_internet_connection
                            )
                        )
                    )
                )
*/
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                  /*  _transactionResponse.postValue(
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
                   /* _transactionResponse.postValue(
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

    private fun handleResponse(response: Response<TransactionResponse>): Event<Resource<TransactionResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}