package com.example.quickpoll.data.network.utils

import android.util.Log
import com.example.quickpoll.data.network.model.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class Resource<T>(data: T? = null, message: String? = null) {
    class Success<T>(val data: T?) : Resource<T>(data)
    class Error<T>(val data: T? = null, val message: String? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}

suspend fun <T> safeApiCall(call: suspend () -> T): Flow<Resource<T>> {
    return flow {
        emit(Resource.Loading());
        try {
            val response = call()
            emit(Resource.Success(data = response))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.message()))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.message))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message))
        }
    }
}