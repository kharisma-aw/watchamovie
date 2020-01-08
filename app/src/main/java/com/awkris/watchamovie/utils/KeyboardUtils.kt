package com.awkris.watchamovie.utils

import android.content.Context
import android.os.IBinder
import android.view.inputmethod.InputMethodManager


fun closeKeyboard(c: Context, windowToken: IBinder?) {
    val mgr = c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    mgr.hideSoftInputFromWindow(windowToken, 0)
}