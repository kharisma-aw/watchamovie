package com.awkris.watchamovie.data.datastore.disk

import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.objectbox.MovieEntity
import com.awkris.watchamovie.data.room.MovieDatabase
import com.awkris.watchamovie.data.room.dao.MovieDao_Impl
import com.nhaarman.mockitokotlin2.whenever
import io.objectbox.Box
import io.objectbox.BoxStore
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
abstract class DiskDataStoreTest {
    @Mock
    lateinit var db: MovieDatabase
    @Mock
    lateinit var boxStore: BoxStore
    @Mock
    lateinit var box: Box<MovieEntity>
    @Mock
    lateinit var movieDao: MovieDao_Impl
    protected lateinit var diskDataStore: DiskMovieDataStore

    @Before
    open fun setUp() {
        diskDataStore = DiskMovieDataStore(db, boxStore, box)
        whenever(db.movieDao()).thenReturn(movieDao)
    }
}