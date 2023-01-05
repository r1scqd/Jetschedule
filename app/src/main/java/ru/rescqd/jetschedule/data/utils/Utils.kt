package ru.rescqd.jetschedule.data.utils

import okhttp3.internal.toLongOrDefault
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun String.isNumber(): Boolean = (this.toLongOrDefault(-101L) != -101L)
val COLLEGE_API_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy")!!
val LocalDate.toApiDate: String
    get() {
        return COLLEGE_API_FORMAT.format(this)
    }
