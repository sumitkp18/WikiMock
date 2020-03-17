package com.enguru.wikiMock.repo

import com.enguru.wikiMock.model.WikiQuery
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface specifying various api calls for querying wikipedia
 * to be used by retrofit
 */
interface WikiAPI {

    /**
     * API to fetch list of results for the query string
     */
    @GET("w/api.php")
    fun getQueryResults(
        @Query("action")
        action: String = "query",
        @Query("format")
        format: String = "json",
        @Query("prop")
        prop: String = "pageimages|pageterms",
        @Query("generator")
        generator: String = "prefixsearch",
        @Query("redirects")
        redirects: Int = 1,
        @Query("formatversion")
        formatVersion: Int = 2,
        @Query("piprop")
        piProp: String = "thumbnail",
        @Query("pithumbsize")
        piThumbSize: Int = 50,
        @Query("pilimit")
        piLimit: Int = 10,
        @Query("wbptterms")
        wbPtTerns: String = "description",
        @Query("gpssearch")
        gpsSearch: String,
        @Query("gpslimit")
        gpsLimit: Int = 10
        ): Observable<WikiQuery>
}