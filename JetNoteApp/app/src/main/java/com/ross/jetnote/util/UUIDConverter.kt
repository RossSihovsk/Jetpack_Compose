package com.ross.jetnote.util

import androidx.room.TypeConverter
import java.util.*

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? = uuid?.toString()

    @TypeConverter
    fun uuidFromString(string: String?): UUID? = UUID.fromString(string)
}