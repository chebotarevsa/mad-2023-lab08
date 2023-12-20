package com.example.lab8mobile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.example.lab8mobile.Data.TermCard
import java.io.ByteArrayOutputStream

const val NEW_CARD = "-1"
interface CallbackFun {
        fun showCard(index: String): Unit
        fun deleteCard(card: TermCard): Unit
    }

class BitmapConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            return null
        }
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        if (byteArray == null || byteArray.isEmpty()) {
            return null
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}