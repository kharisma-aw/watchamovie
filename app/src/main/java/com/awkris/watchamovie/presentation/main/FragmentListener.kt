package com.awkris.watchamovie.presentation.main

interface FragmentListener {
    fun navigateToMovieDetail(movieId: Int, disableReminder: Boolean = false)
    fun navigateToSearch()
}