package com.example.androidapp.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.text.DateFormat
import java.util.Date
import android.util.Base64

fun Long.convertDateToString(): String {
    return DateFormat.getDateInstance().format(Date(this))
}

fun String.decodeThumbnailImage(): Bitmap? {
    return try {
        val byteArray = Base64.decode(this.cleanupImageString(), Base64.NO_WRAP)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    } catch (e: Exception) {
        null
    }
}

fun String.cleanupImageString(): String {
    return this.replace("data:image/png;base64,", "")
        .replace("data:image/jpeg;base64,", "")
}
