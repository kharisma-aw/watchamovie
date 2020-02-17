package com.awkris.watchamovie.utils.storage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class SessionSharedPreferences(context: Context, moshi: Moshi) {
    private val defaultList: String
    private val jsonAdapter: JsonAdapter<LastKeyword>
    private val sharedPreferences: SharedPreferences
    private val sharedPrefChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
    private val sharedPrefSubject: BehaviorSubject<SharedPreferences>
    private val storageName = "sessionstorage"

    init {
        jsonAdapter = moshi.adapter(LastKeyword::class.java)
        defaultList = jsonAdapter.toJson(LastKeyword(emptyList()))
        sharedPreferences = context.getSharedPreferences(storageName, Context.MODE_PRIVATE)
        sharedPrefSubject = BehaviorSubject.createDefault(sharedPreferences)
        sharedPrefChangeListener = SharedPreferences
            .OnSharedPreferenceChangeListener { sharedPref, _ ->
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
        Timber.d("fetching last keyword")
        return sharedPrefSubject.map {
            jsonAdapter.fromJson(it.getString(LAST_KEYWORD, defaultList)!!)!!.keywords
        }
    }

    fun addLastKeyword(keyword: String): Completable {
        Timber.d("adding last keyword")
        val currentSavedKeywords = jsonAdapter.fromJson(
            sharedPreferences.getString(LAST_KEYWORD, defaultList)!!
        )!!.keywords
        val newList = currentSavedKeywords.toMutableList()
        if (newList.find { it == keyword } != null) {
            newList.remove(keyword)
        } else if (newList.size >= LAST_KEYWORD_SIZE){
            newList.removeAt(0)
        }
        newList.add(keyword)
        return sharedPrefSubject.firstOrError().flatMapCompletable {
            Completable.fromAction {
                it.edit()
                    .putString(LAST_KEYWORD, jsonAdapter.toJson(LastKeyword(newList)))
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