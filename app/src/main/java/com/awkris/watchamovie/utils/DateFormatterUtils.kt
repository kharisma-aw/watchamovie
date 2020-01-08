package com.awkris.watchamovie.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun formatReleaseYear(s: String): String {
    val responsePattern = SimpleDateFormat(DateFormat.RESPONSE_FORMAT_PATTERN)
    val yearPattern = SimpleDateFormat(DateFormat.YEAR_FORMAT_PATTERN)
    return yearPattern.format(requireNotNull(responsePattern.parse(s)))
}

@SuppressLint("SimpleDateFormat")
fun formatDate(s: String): Date {
    val responsePattern = SimpleDateFormat(DateFormat.RESPONSE_FORMAT_PATTERN)
    return responsePattern.parse(s)!!
}

object DateFormat {
    const val RESPONSE_FORMAT_PATTERN = "yyyy-MM-dd"
    const val YEAR_FORMAT_PATTERN = "yyyy"
}