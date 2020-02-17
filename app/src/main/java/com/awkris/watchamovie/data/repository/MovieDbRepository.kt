package com.awkris.watchamovie.data.repository

import androidx.paging.DataSource
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.datastore.DiskMovieDataStore
import com.awkris.watchamovie.data.model.MovieDetailWithAdditionalInfo
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.*
import com.awkris.watchamovie.data.room.entity.Movie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.functions.Function3
import java.util.concurrent.TimeUnit

class MovieDbRepository(
    private val cloudMovieDataStore: CloudMovieDataStore,
    private val diskMovieDataStore: DiskMovieDataStore
) {
    fun getCrewCredits(personId: Int): Single<List<CastDetailsResponse>> {
        return cloudMovieDataStore.getCrewCredits(personId).map { it.cast }
    }

    fun getMovieDetailWithAdditionalInfo(movieId: Int): Single<MovieDetailWithAdditionalInfo> {
        return cloudMovieDataStore.let {
            val movieDetail = it.getMovieDetail(movieId)
            val casts = it.getCredits(movieId).map { response -> response.casts }
            val recommendations = it.getRecommendations(movieId)
            Single.zip(
                movieDetail,
                casts,
                recommendations,
                Function3<MovieDetailResponse, List<Cast>, List<MovieResponse>, MovieDetailWithAdditionalInfo> { t1, t2, t3 ->
                    MovieDetailWithAdditionalInfo(t1, t2, t3)
                }
            )
        }
    }

    fun getNowPlayingList(region: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.getNowPlayingList(region, page)
    }

    fun getPersonDetail(personId: Int): Single<PersonDetailResponse> {
        return cloudMovieDataStore.getPersonDetail(personId)
    }

    fun searchMovie(query: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.searchMovie(query, page)
    }

    fun getUpcomingList(region: String, page: Int? = null): Single<PaginatedList<MovieResponse>> {
        return cloudMovieDataStore.getUpcomingList(region, page)
    }

    fun saveToWatchlist(movie: MovieDetailResponse): Maybe<Long> {
        return diskMovieDataStore.saveToWatchlist(movie)
    }

    fun deleteMovie(id: Int): Completable {
        return diskMovieDataStore.deleteMovie(id)
    }

    fun findMovie(id: Int): Maybe<Movie> {
        return diskMovieDataStore.findMovie(id)
    }

    fun getAllReminders(): Single<List<Movie>> {
        return diskMovieDataStore.getAllReminders()
    }

    fun getWatchList(): DataSource.Factory<Int, Movie> {
        return diskMovieDataStore.getWatchList()
    }

    fun updateReminder(id: Int, setReminder: Boolean): Completable {
        return diskMovieDataStore.updateReminder(id, setReminder)
    }
}