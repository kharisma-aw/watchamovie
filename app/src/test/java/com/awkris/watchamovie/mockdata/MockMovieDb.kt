package com.awkris.watchamovie.mockdata

import com.awkris.watchamovie.data.model.PaginatedList
import com.awkris.watchamovie.data.model.response.*

private val MOVIE_1 = MovieResponse(
    id = 24428,
    title = "The Avengers",
    originalTitle = "The Avengers",
    originalLanguage = "en",
    overview = "When an unexpected enemy emerges and threatens global safety and security, Nick Fury, director of the international peacekeeping agency known as S.H.I.E.L.D., finds himself in need of a team to pull the world back from the brink of disaster. Spanning the globe, a daring recruitment effort begins!",
    genreIds = listOf(878, 28, 12),
    video = false,
    backdropPath = "/hbn46fQaRmlpBuUrEiFqv0GDL6Y.jpg",
    posterPath = "/cezWGskPY5x7GaglTTRN4Fugfb8.jpg",
    releaseDate = "2012-04-25",
    popularity = 7.353212,
    voteAverage = 7.33,
    voteCount = 8503,
    adult = false
)

private val MOVIE_2 = MovieResponse(
    id = 9320,
    title = "The Avengers",
    originalTitle = "The Avengers",
    originalLanguage = "en",
    overview = "British Ministry agent John Steed, under direction from \"Mother\", investigates a diabolical plot by arch-villain Sir August de Wynter to rule the world with his weather control machine. Steed investigates the beautiful Doctor Mrs. Emma Peel, the only suspect, but simultaneously falls for her and joins forces with her to combat Sir August.",
    genreIds = listOf(53),
    video = false,
    backdropPath = "/8YW4rwWQgC2JRlBcpStMNUko13k.jpg",
    posterPath = "/7cJGRajXMU2aYdTbElIl6FtzOl2.jpg",
    releaseDate = "1998-08-13",
    popularity = 2.270454,
    voteAverage = 4.7,
    voteCount = 111,
    adult = false
)

val PAGINATED_MOVIE_LIST = PaginatedList(listOf(MOVIE_1, MOVIE_2), 1, 1, 14)

val MOVIE_DETAIL = MovieDetailResponse(
    adult = false,
    backdropPath = "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg",
    belongsToCollection = null,
    budget = 63000000,
    genres = listOf(Genre(18, "Drama")),
    homepage = "",
    id = 550,
    imdbId = "tt0137523",
    originalLanguage = "en",
    originalTitle = "Fight Club",
    overview = "A ticking-time-bomb insomniac and a slippery soap salesman channel primal male aggression into a shocking new form of therapy. Their concept catches on, with underground \"fight clubs\" forming in every town, until an eccentric gets in the way and ignites an out-of-control spiral toward oblivion.",
    popularity = 0.5,
    posterPath = null,
    productionCompanies = listOf(
        Company(
            25,
            "/qZCc1lty5FzX30aOCVRBLzaVmcp.png",
            "20th Century Fox",
            "US"
        )
    ),
    productionCountries = listOf(Country("US", "United States of America")),
    releaseDate = "1999-10-12",
    revenue = 100853753,
    runtime = 139,
    spokenLanguages = listOf(Language("en", "English")),
    status = "Released",
    tagline = "How much can you know about yourself if you've never been in a fight?",
    title = "Fight Club",
    video = false,
    voteAverage = 7.8,
    voteCount = 3439
)