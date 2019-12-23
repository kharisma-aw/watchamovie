package com.awkris.watchamovie.data.api

import com.awkris.watchamovie.data.api.utils.UrlConstants
import com.awkris.watchamovie.data.datastore.CloudMovieDataStore
import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.MovieDetailResponse
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.mockdata.MOVIE_DETAIL
import com.awkris.watchamovie.mockdata.PAGINATED_MOVIE_LIST
import com.awkris.watchamovie.testutils.RequestAssertionUtils
import com.awkris.watchamovie.testutils.assertSuccessObserver
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CloudMovieDataStoreTest : CloudDataStoreTest() {
    private lateinit var cloudMovieDataStore: CloudMovieDataStore

    @Before
    override fun setUp() {
        super.setUp()
        cloudMovieDataStore = CloudMovieDataStore(createApi(MovieDbApi::class.java))
    }

    @Test
    fun `get now-playing movie list successfully`() {
        enqueueSuccessResponse("success/get/now_playing.json")

        val observer = TestObserver.create<PaginatedList<MovieResponse>>()
        cloudMovieDataStore.getNowPlayingList("ID", 1).subscribe(observer)

        RequestAssertionUtils(takeRequest()).run {
            assertRequestLine(
                RequestAssertionUtils.Method.GET,
                UrlConstants.NOW_PLAYING
            )
            assertNoBodyParam()
            assertTotalQueryParams(3)
            assertQueryParam("api_key", CloudMovieDataStore.KEY)
            assertQueryParam("region", "ID")
            assertQueryParam("page", 1)
        }
        observer.assertSuccessObserver(1)
        val expectedResult = PAGINATED_MOVIE_LIST
        val actualResult = observer.values()[0]
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `search movie successfully`() {
        enqueueSuccessResponse("success/get/search_movie.json")

        val observer = TestObserver.create<PaginatedList<MovieResponse>>()
        cloudMovieDataStore.searchMovie("avengers", 1).subscribe(observer)

        RequestAssertionUtils(takeRequest()).run {
            assertRequestLine(
                RequestAssertionUtils.Method.GET,
                UrlConstants.SEARCH_MOVIE
            )
            assertNoBodyParam()
            assertTotalQueryParams(3)
            assertQueryParam("api_key", CloudMovieDataStore.KEY)
            assertQueryParam("query", "avengers")
            assertQueryParam("page", 1)
        }
        observer.assertSuccessObserver(1)
        val expectedResult = PAGINATED_MOVIE_LIST
        val actualResult = observer.values()[0]
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `get movie detail successfully`() {
        enqueueSuccessResponse("success/get/movie_detail.json")

        val movieId = 550
        val observer = TestObserver.create<MovieDetailResponse>()
        cloudMovieDataStore.getMovieDetail(movieId).subscribe(observer)

        RequestAssertionUtils(takeRequest()).run {
            assertRequestLine(
                RequestAssertionUtils.Method.GET,
                UrlConstants.MOVIE_DETAIL,
                movieId.toString()
            )
            assertNoBodyParam()
            assertTotalQueryParams(1)
            assertQueryParam("api_key", CloudMovieDataStore.KEY)
        }
        observer.assertSuccessObserver(1)
        val expectedResult = MOVIE_DETAIL
        val actualResult = observer.values()[0]
        Assert.assertEquals(expectedResult, actualResult)
    }
}