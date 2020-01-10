package com.awkris.watchamovie.data.datastore.disk

import androidx.paging.DataSource
import com.awkris.watchamovie.data.room.entity.Movie
import com.awkris.watchamovie.data.room.mapper.transform
import com.awkris.watchamovie.mockdata.MOVIE_DETAIL
import com.awkris.watchamovie.testutils.assertCompleteWithoutErrors
import com.awkris.watchamovie.testutils.assertSuccessObserver
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DiskMovieDataStoreTest : DiskDataStoreTest() {

    @Test
    fun `save movie to database successfully`() {
        val movie = transform(MOVIE_DETAIL)
        val observer = TestObserver.create<Completable>()
        whenever(movieDao.insert(movie)).thenReturn(Completable.complete())

        diskDataStore.saveToWatchlist(MOVIE_DETAIL).subscribe(observer)

        observer.assertCompleteWithoutErrors()
    }

    @Test
    fun `delete movie from database successfully`() {
        val observer = TestObserver.create<Completable>()
        whenever(movieDao.deleteById(MOVIE_DETAIL.id)).thenReturn(Completable.complete())

        diskDataStore.deleteMovie(MOVIE_DETAIL.id).subscribe(observer)

        observer.assertCompleteWithoutErrors()
    }

    @Test
    fun `get movie by id successfully`() {
        val movie = transform(MOVIE_DETAIL)
        val observer = TestObserver.create<Movie>()
        whenever(movieDao.getMovie(movie.id)).thenReturn(Maybe.just(movie))

        diskDataStore.findMovie(movie.id).subscribe(observer)

        observer.assertSuccessObserver(1)
        observer.assertValue(movie)
    }

    @Test
    fun `update reminder flag successfully`() {
        val movie = transform(MOVIE_DETAIL)
        val observer = TestObserver.create<Completable>()
        whenever(movieDao.updateReminder(movie.id, false))
            .thenReturn(Completable.complete())

        diskDataStore.updateReminder(movie.id, false).subscribe(observer)

        observer.assertCompleteWithoutErrors()
    }

    @Test
    fun `get all reminders successfully`() {
        val list = listOf(transform(MOVIE_DETAIL))
        val observer = TestObserver.create<List<Movie>>()
        whenever(movieDao.getAllReminders()).thenReturn(Single.just(list))

        diskDataStore.getAllReminders().subscribe(observer)

        observer.assertCompleteWithoutErrors()
        observer.assertValue(list)
    }
}