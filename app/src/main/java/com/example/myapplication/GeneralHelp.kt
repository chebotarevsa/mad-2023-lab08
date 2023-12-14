package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

fun Uri?.bitmap(context: Context): Bitmap? {
    return this?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, it))
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, it)
        }
    }
}

class Converters {
    @TypeConverter
    fun fromBitmapToByteArray(value: Bitmap?): ByteArray? {
        val stream = ByteArrayOutputStream()
        value?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    @TypeConverter
    fun fromByteArrayToBitmap(value: ByteArray?): Bitmap? {
        return value?.let { BitmapFactory.decodeByteArray(value, 0, it.size) }
    }
}

interface ActionInterface {
    fun onItemClick(cardId: String)
    fun onDeleteCard(cardId: String)
}


