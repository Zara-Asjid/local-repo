package com.sait.tawajudpremiumplusnewfeatured.ui.register.viewmodels.EmailValidity

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sait.tawajudpremiumplusnewfeatured.R
import androidx.lifecycle.viewModelScope
import com.sait.tawajudpremiumplusnewfeatured.TawajudApplication
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Event
import com.sait.tawajudpremiumplusnewfeatured.data.remote.Resource
import com.sait.tawajudpremiumplusnewfeatured.ui.register.models.EmailValidityResponse
import com.sait.tawajudpremiumplusnewfeatured.util.extension.hasInternetConnection
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class EmailValidityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EmailValidityRepository(EmailValidityDataSource())
    private val _registerResponse = MutableLiveData<Event<Resource<EmailValidityResponse>>>()
    val registerResponse: LiveData<Event<Resource<EmailValidityResponse>>> = _registerResponse


    fun getRegisterData(email: String, mcontext :Context) = viewModelScope.launch {

        _registerResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getRegisterData(email,mcontext )
                _registerResponse.postValue(handleResponse(response))
            } else {
                _registerResponse.postValue(
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
                    /*  _registerResponse.postValue(
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
                    /*    _registerResponse.postValue(
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

    private fun handleResponse(response: Response<EmailValidityResponse>): Event<Resource<EmailValidityResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}