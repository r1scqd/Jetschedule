package ru.rescqd.jetschedule.data.local.database.conventer

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverter {
    @TypeConverter
    fun toLong(it: LocalDate) = it.toEpochDay()
    @TypeConverter
    fun toLocalDate(it: Long) = LocalDate.ofEpochDay(it)!!
}