package com.finance.growwassignment.utilities
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): String? {
        if (value == null) {
            return null
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault())
        return dateFormat.format(Date(value))
    }

    @TypeConverter
    fun toTimestamp(value: String?): Long? {
        if (value == null) {
            return null
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault())

        return try {
            val date = dateFormat.parse(value)
            date?.time ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
