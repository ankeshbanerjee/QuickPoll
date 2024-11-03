package com.example.quickpoll.data.network.service

import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.model.upload.UploadResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {
    @Multipart
    @POST("upload")
    suspend fun upload(
        @Part image: MultipartBody.Part
    ): Response<ApiResponse<UploadResult>>
}