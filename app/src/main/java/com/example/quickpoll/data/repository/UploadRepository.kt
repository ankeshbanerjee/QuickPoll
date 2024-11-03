package com.example.quickpoll.data.repository

import com.example.quickpoll.data.network.service.UploadService
import com.example.quickpoll.data.network.utils.safeApiCall
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadRepository @Inject constructor(
    private val uploadService: UploadService
) {
    suspend fun upload(file: File) = safeApiCall {
        uploadService.upload(
            image = MultipartBody.Part.createFormData(
                name = "image",
                filename = file.name,
                body = file.asRequestBody()
            )
        )
    }
}