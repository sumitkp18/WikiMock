package com.enguru.wikiMock.repo.database

import com.enguru.wikiMock.model.CacheResults
import com.enguru.wikiMock.model.Page
import com.enguru.wikiMock.model.Thumbnail
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import java.sql.Timestamp

/**
 * A Realm based database manager with caching
 */
object WikiDatabaseManager {

    private var configuration: RealmConfiguration
    const val SCHEMA_VERSION = 1L
    const val DATABASE_NAME = "wikiRepo.realm"
    const val CACHE_EXPIRATION_TIME = 2 * 60 * 60 * 1000  //2 hours

    init {
        configuration = RealmConfiguration.Builder().schemaVersion(SCHEMA_VERSION).name(DATABASE_NAME).build()
        Realm.setDefaultConfiguration(configuration)
    }

    /**
     * deletes existing data for the specified query from DB
     * @param queryString the query string for which the existing DB data is to be deleted
     */
    private fun deleteExistingCache(queryString: String) {
        getRealmInstance().transaction { realm ->
            val cacheResult = configuration.query<CacheResults> {
                equalTo(CacheResults.QUERY_STRING, queryString)
            }
            cacheResult.forEach { cacheData ->
                cacheData.let {
                    it.data?.forEach { page ->
                        page?.let { thumbnail ->
                            realm.deleteById(Thumbnail::class.java, thumbnail.id)
                            realm.deleteById(Thumbnail::class.java, thumbnail.id)
                        }
                        realm.deleteById(Page::class.java, page.id)
                    }
                    realm.deleteById(CacheResults::class.java, it.id)
                }
            }
        }
    }

    /**
     *
     * @param timestamp timestamp when the data was cached
     */
    private fun hasNotExpired(timestamp: Long?): Boolean {
        return timestamp?.let {
            System.currentTimeMillis().minus(it) < CACHE_EXPIRATION_TIME
        } ?: true
    }

    /**
     * fetches queryString results from DB
     */
    fun getQueryResults(queryString: String): List<Page> {
        val combinedResult = ArrayList<Page>()
        val cacheResult = configuration.query<CacheResults> {
            equalTo(CacheResults.QUERY_STRING, queryString)
        }
        cacheResult.filter { hasNotExpired(it.timestamp) && it.query?.contains(queryString) ?: false }.map { cacheResults ->
            cacheResults.data?.toList()?.let { combinedResult.addAll(it) }
        }
        return combinedResult
    }

    /**
     * Inserts query results into DB.
     */
    fun insertQueryResults(query: String, resultList: List<Page>) {
        deleteExistingCache(query)
        val realmList = RealmList<Page>()
        resultList.forEach { realmList.add(it) }
        CacheResults(query = query, data = realmList, timestamp = System.currentTimeMillis()).save()
    }
}