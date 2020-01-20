package com.awkris.watchamovie.presentation.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

open class BaseViewModel : ViewModel() {
    protected val scope = CoroutineScope(Job() + Dispatchers.IO)

    open fun clear() {
        scope.cancel()
    }
}