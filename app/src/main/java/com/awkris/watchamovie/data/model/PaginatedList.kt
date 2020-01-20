package com.awkris.watchamovie.data.model

data class PaginatedList<out T>(
    val list: List<T>,
    val page: Int,
    val totalPages: Int,
    val totalItems: Int
)