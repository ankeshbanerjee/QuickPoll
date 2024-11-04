package com.example.quickpoll.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

enum class UiState {
    LOADING,
    SUCCESS,
    ERROR,
    REFRESHING,
    IDLE,
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun roundOffDecimal(number: Float): Float {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(number).toFloat()
}

fun getFileFromUri(contentResolver: ContentResolver,uri: Uri): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
    cursor?.moveToFirst()
    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
    val filePath = cursor?.getString(columnIndex!!)
    cursor?.close()
    return filePath?.let { File(it) }
}