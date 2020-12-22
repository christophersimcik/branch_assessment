package com.simcik.branchassessment.custom.helpers

import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {
    private const val DATE_FORMAT = "MMM dd, yyyy"

    fun getTime(): Long{
        return Calendar.getInstance().timeInMillis
    }

    fun getTimeAsString(time: Long): String{
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return simpleDateFormat.format((Calendar.getInstance().apply{timeInMillis = time}).time)
    }

}