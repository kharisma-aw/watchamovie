package com.awkris.watchamovie.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.awkris.watchamovie.data.room.dao.*
import com.awkris.watchamovie.data.room.entity.*


@Database(
    entities = [Movie::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        const val DB_NAME = "moviedatabase.db"
    }
}