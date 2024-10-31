package com.example.quickpoll.data.network.utils

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class Resource<T>(data: T? = null, message: String? = null) {
    class Success<T>(val data: T) : Resource<T>(data)
    class Error<T>(val data: T? = null, val message: String? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

suspend fun <T> safeApiCall(call: suspend () -> T): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading());
        try {
            val response = call()
            Log.d("safeApiCall", response.toString())
            if((response as Response<*>).isSuccessful){
                emit(Resource.Success(data = response))
            }else {
                emit(Resource.Error(message = response.message()))
            }
        } catch (e: HttpException) {
            Log.d("safeApiCallError", e.message())
            emit(Resource.Error(message = e.message()))
        } catch (e: IOException) {
            Log.d("safeApiCallError", e.message?: "IOException")
            emit(Resource.Error(message = e.message))
        } catch (e: Exception) {
            Log.d("safeApiCallError", e.message ?: "Exception")
            emit(Resource.Error(message = e.message))
        }
    }
}