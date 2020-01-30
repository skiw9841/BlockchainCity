package com.ad4th.seoulandroid.api.intro

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Candidate {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
    @SerializedName("votes")
    @Expose
    var votes: Int? = null
    @SerializedName("ranking")
    @Expose
    var ranking: Int? = null
    @SerializedName("proportion")
    @Expose
    var proportion: Double? = null
    @SerializedName("walletAddress")
    @Expose
    var walletAddress: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null

}