package com.awkris.watchamovie.data.datastore.disk

import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.data.room.dao.MovieDao_Impl
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class DiskDataStoreTest {
    @Mock
    lateinit var db: MovieDatabase
    @Mock
    lateinit var movieDao: MovieDao_Impl
    protected lateinit var diskDataStore: DiskMovieDataStore

    @Before
    fun setUp() {
        diskDataStore = DiskMovieDataStore(db)
        whenever(db.movieDao()).thenReturn(movieDao)
    }
}