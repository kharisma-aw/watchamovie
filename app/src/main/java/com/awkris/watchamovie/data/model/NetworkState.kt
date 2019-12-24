package com.awkris.watchamovie.data.model

sealed class NetworkState {
    object Loading : NetworkState()
    object Success : NetworkState()
    class Error(val message: String?) : NetworkState()
}