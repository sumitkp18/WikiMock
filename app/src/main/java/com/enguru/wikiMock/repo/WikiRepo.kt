package com.enguru.wikiMock.repo

import com.enguru.wikiMock.model.Page
import io.reactivex.Observable

/**
 * Interface specifying methods for fetching details
 */
interface WikiRepo {

    /**
     * method to fetch list of results of a query
     * @param query the query string for searching results from wikipedia
     * @param fetchFromServer a boolean which tells whether to fetch from cache or server
     */
    fun getQueryResults(query: String, fetchFromServer: Boolean = false): Observable<List<Page>>
}