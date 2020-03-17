package com.enguru.wikiMock.ui

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.enguru.wikiMock.model.Page
import com.enguru.wikiMock.repo.WikiRepo
import io.reactivex.disposables.CompositeDisposable

/**
 * ViewModel class for the [SearchActivity]
 */
class SearchViewModel(private val repo: WikiRepo) : ViewModel() {
    private val TAG = SearchViewModel::class.java.simpleName
    val networkError  = ObservableBoolean()
    val fetchedResults: LiveData<List<Page>>
        get() = searchResults
    private val searchResults = MutableLiveData<List<Page>>()
    private val compositeDisposable = CompositeDisposable()

    /**
     * method to fetch the list of query results
     */
    fun fetchQueryResults(query: String = "", fetchFromServer: Boolean = false) {
        networkError.set(false)
        val disposable = repo.getQueryResults(
            query = query,
            fetchFromServer = fetchFromServer
        ).subscribe({
            searchResults.postValue(it)
        },
            { t ->
                networkError.set(true)
                Log.d(TAG, t.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    /**
     * clears the [CompositeDisposable]
     */
    override fun onCleared() {
        compositeDisposable.clear()
    }

}