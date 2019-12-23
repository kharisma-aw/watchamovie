package com.awkris.watchamovie.data.api.utils

import timber.log.Timber

fun log(t: Throwable) {
    Timber.d("Error encountered: ${t.message}\nCause: ${t.cause}")
}