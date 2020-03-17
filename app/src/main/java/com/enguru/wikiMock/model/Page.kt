package com.enguru.wikiMock.model

import com.enguru.wikiMock.repo.database.generateUUID
import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Page(
    @PrimaryKey
    var id: String = generateUUID(),
    @SerializedName("pageid")
    var pageId: Int? = null,
    var ns: Int? = null,
    var title: String? = null,
    var index: Int? = null,
    var thumbnail: Thumbnail? = null,
    var terms: Terms? = null
) : RealmObject()

open class Thumbnail(
    var id: String = generateUUID(),
    var source: String? = null,
    var height: Int? = null,
    var width: Int? = null
) : RealmObject()

open class Terms(
    var id: String = generateUUID(),
    var description: RealmList<String>? = null
) : RealmObject()
