package com.awkris.watchamovie.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private const val HOURS_IN_A_DAY = 24
private const val RESPONSE_FORMAT_PATTERN = "yyyy-MM-dd"
private const val YEAR_FORMAT_PATTERN = "yyyy"

@SuppressLint("SimpleDateFormat")
fun formatReleaseYear(s: String): String {
    val responsePattern = SimpleDateFormat(RESPONSE_FORMAT_PATTERN)
    val yearPattern = SimpleDateFormat(YEAR_FORMAT_PATTERN)
    return yearPattern.format(requireNotNull(responsePattern.parse(s)))
}

@SuppressLint("SimpleDateFormat")
fun formatDate(s: String): Date {
    val responsePattern = SimpleDateFormat(RESPONSE_FORMAT_PATTERN)
    return responsePattern.parse(s)!!
}

fun isUpcoming(s: String): Boolean {
    val date = formatDate(s).time
    val timeDiff = date - System.currentTimeMillis()
    return timeDiff > 0 && TimeUnit.MILLISECONDS.toHours(timeDiff) > HOURS_IN_A_DAY
}