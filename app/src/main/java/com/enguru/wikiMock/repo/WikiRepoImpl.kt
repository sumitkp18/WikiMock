package com.enguru.wikiMock.repo

import com.enguru.wikiMock.model.Page
import com.enguru.wikiMock.repo.database.WikiDatabaseManager
import com.enguru.wikiMock.util.AppRxSchedulers
import com.enguru.wikiMock.util.RxNetwork
import io.reactivex.Observable

/**
 * Implementation class of the [WikiRepo] Interface
 */
class WikiRepoImpl(private val wikiAPI: WikiAPI) : WikiRepo {

    /**
     * Method to fetch the list of results for the given query string
     * Based on Internet connectivity it fetches data from API call or local cache
     */
    override fun getQueryResults(query: String, fetchFromServer: Boolean): Observable<List<Page>> {
        if (fetchFromServer) {
            return RxNetwork.isInternetAvailable().toObservable().concatMap { isInternetAvailable ->
                if (isInternetAvailable) {
                    wikiAPI.getQueryResults(gpsSearch = query)
                        .subscribeOn(AppRxSchedulers.network()).concatMap { wikiQuery ->
                            wikiQuery.query?.pages?.let {
                                WikiDatabaseManager.insertQueryResults(query = query, resultList = it)
                            Observable.just(it)
                            }
                        }
                } else {
                    RxNetwork.getNetworkNotAvailable(type = List::class.java) as Observable<List<Page>>
                }
            }
        } else {
            WikiDatabaseManager.getQueryResults(query)?.let {
                return if (it.isEmpty())
                    getQueryResults(query, true)
                else
                    Observable.just(it)
            }
        }
    }

}