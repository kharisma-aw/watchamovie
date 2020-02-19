package com.awkris.watchamovie.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.data.model.response.MovieResponse
import com.awkris.watchamovie.utils.decrypt
import com.awkris.watchamovie.utils.encrypt
import com.awkris.watchamovie.utils.storage.SessionSharedPreferences
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.get
import timber.log.Timber
import java.security.KeyStore

class SearchViewModel(
    private val dataSourceFactory: SearchDataSourceFactory,
    private val sharedPref: SessionSharedPreferences
) : ViewModel(), KoinComponent {
    private val _lastKeywords = MutableLiveData<List<String>>()
    val lastKeywords: LiveData<List<String>>
        get() = _lastKeywords

    val networkState: LiveData<NetworkState>
    val searchList: LiveData<PagedList<MovieResponse>>

    val compositeDisposable = CompositeDisposable()

    private val key = get<KeyStore.PrivateKeyEntry>()

    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(20)
        .setPageSize(20)
        .build()

    init {
        networkState = Transformations.switchMap(dataSourceFactory.getDataSource()) { dataSource ->
            dataSource.networkState
        }
        searchList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
        getLastKeywords()
    }

    fun search(keyword: String? = null) {
        dataSourceFactory.recreate(keyword)
    }

    fun dispose() {
        dataSourceFactory.invalidate()
        if (!compositeDisposable.isDisposed) compositeDisposable.dispose()
    }

    fun saveKeyword(keyword: String) {
        val encrypted = encrypt(keyword.toByteArray(Charsets.UTF_8), key.privateKey)
        val decrypted = decrypt(encrypted!!, key.certificate.publicKey)
        Timber.d("encrypted string: %s\ndecrypted string: %s", String(encrypted, Charsets.UTF_8), String(decrypted!!, Charsets.UTF_8))
        sharedPref.addLastKeyword(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : CompletableObserver {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onError(e: Throwable) {
                    }
                }
            )
    }

    private fun getLastKeywords() {
        sharedPref.getLastKeywords()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<List<String>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.add(d)
                    }

                    override fun onNext(t: List<String>) {
                        Timber.d("Fetched keywords:\n%s", t.toString())
                        _lastKeywords.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                    }
                }
            )
    }
}