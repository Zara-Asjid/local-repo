package com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.viewmodels

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
import com.sait.tawajudpremiumplusnewfeatured.ui.changePassword.models.ChangePasswordResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException


class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {



    private val repository = ChangePasswordRepository(ChangePasswordDataSource())

    private val _changePasswordResponse = MutableLiveData<Event<Resource<ChangePasswordResponse>>>()
    val changePasswordResponse: LiveData<Event<Resource<ChangePasswordResponse>>> = _changePasswordResponse

    fun getchangePasswordData(
        mContext: Context, userId: Int,
        username: String, newpassword: String, oldpassword: String, lang: Int) = viewModelScope.launch {

        _changePasswordResponse.postValue(Event(Resource.Loading()))

        try {
            if (getApplication<TawajudApplication>().hasInternetConnection()) {
                val response = repository.getChangePasswordData(mContext,userId,username,newpassword,oldpassword,lang )
                _changePasswordResponse.postValue(handleResponse(response))
            } else {
                _changePasswordResponse.postValue(
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
                  /*  _changePasswordResponse.postValue(
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
               /*     _changePasswordResponse.postValue(
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

    private fun handleResponse(response: Response<ChangePasswordResponse>): Event<Resource<ChangePasswordResponse>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Event(Resource.Success(resultResponse))
            }
        }
        return Event(Resource.Error(response.message()))
    }





}