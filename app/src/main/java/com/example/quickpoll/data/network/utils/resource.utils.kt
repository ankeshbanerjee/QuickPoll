package com.example.quickpoll.data.network.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class Resource<T>(data: T? = null, message: String? = null) {
    class Success<T>(val data: T) : Resource<T>(data)
    class Error<T>(val data: T? = null, val message: String? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

fun <T> safeApiCall(call: suspend () -> T): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading());
        val response = call()
        Log.d("safeApiCall", response.toString())
        if ((response as Response<*>).isSuccessful) {
            emit(Resource.Success(data = response))
        } else {
            emit(Resource.Error(message = response.message()))
        }
    }.catch {e->
        Log.e("safeApiCallError", e.message ?: "Unknown error")
        when (e) {
            is HttpException -> emit(Resource.Error(message = e.message()))
            is IOException -> emit(Resource.Error(message = e.message ?: "IOException"))
            else -> emit(Resource.Error(message = e.message ?: "Exception"))
        }
    } as Flow<Resource<T>>
}