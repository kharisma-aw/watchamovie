package com.awkris.watchamovie.utils.storage

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class SessionSharedPreferences(context: Context, moshi: Moshi) {
    private val storageName = "sessionstorage"

    private val jsonAdapter = moshi.adapter(LastKeyword::class.java)
    private val sharedPreferences = context.getSharedPreferences(storageName, Context.MODE_PRIVATE)

    private val defaultList = jsonAdapter.toJson(LastKeyword(emptyList()))
    private val sharedPrefSubject = BehaviorSubject.createDefault(sharedPreferences)

    private val sharedPrefChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        sharedPrefChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPref, _ ->
                sharedPrefSubject.onNext(sharedPref)
            }
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefChangeListener)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(TOKEN, null)
    }

    fun putToken(token: String) {
        sharedPreferences.edit().putString(TOKEN, token).apply()
    }

    fun getLastKeywords(): Observable<List<String>> {
        return sharedPrefSubject.map {
            jsonAdapter.fromJson(requireNotNull(it.getString(LAST_KEYWORD, defaultList)))!!.keywords
                .takeLast(LAST_KEYWORD_SIZE)
        }
    }

    fun addLastKeyword(keyword: String): Completable {
        val currentSavedKeywords = jsonAdapter.fromJson(
            requireNotNull(sharedPreferences.getString(LAST_KEYWORD, defaultList))
        )!!.keywords

        val newList = currentSavedKeywords.toMutableList().apply {
            if (find { it == keyword } != null) remove(keyword)
            add(keyword)
        }

        return sharedPrefSubject.firstOrError().flatMapCompletable {
            Completable.fromAction {
                it.edit()
                    .putString(
                        LAST_KEYWORD,
                        jsonAdapter.toJson(LastKeyword(newList.takeLast(LAST_KEYWORD_SIZE)))
                    )
                    .apply()
            }
        }
    }

    private data class LastKeyword(
        @Json(name = "keywords")
        val keywords: List<String>
    )

    companion object {
        private const val LAST_KEYWORD_SIZE = 5
        private const val LAST_KEYWORD = "LAST_KEYWORD"
        private const val TOKEN = "TOKEN"
    }
}