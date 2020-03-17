package com.enguru.wikiMock.model

import com.enguru.wikiMock.repo.database.generateUUID
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Model class for caching list of query results
 */
open class CacheResults(
    @PrimaryKey
    var id: String? = generateUUID(),
    var query: String? = null,
    var data: RealmList<Page>? = null,
    var timestamp: Long? = null
) : RealmObject() {
    companion object {
        const val QUERY_STRING = "query"
    }
}