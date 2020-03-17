package com.enguru.wikiMock.model

import com.google.gson.annotations.SerializedName

open class WikiQuery(
    @SerializedName("batchcomplete")
    var batchComplete: Boolean? = null,
    @SerializedName("continue")
    var `continue`: Continue? = null,
    var query: Query? = null

)

open class Continue(
    @SerializedName("gpsoffset")
    var gpsOffset: Int? = null,
    @SerializedName("continue")
    var `continue`: String? = null
)

open class Query(
    var redirects: List<Redirects>? = null,
    var pages: List<Page>? = null
)

open class  Redirects(
    var index: Int? = null,
    var from: String? = null,
    var to: String? = null
)