package com.ad4th.seoulandroid.api.intro

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Voting {
    // sequence:0, title:"아이콘리퍼블릭", description:"", imageUrl:"R."
    @SerializedName("sequence")
    @Expose
    var sequence: Int? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null
    @SerializedName("numberOfVoters")
    @Expose
    var numberOfVoters: Int? = null
    @SerializedName("administrator")
    @Expose
    var administrator: String? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null
    @SerializedName("updatedAt")
    @Expose
    var updatedAt: String? = null
    @SerializedName("candidates")
    @Expose
    lateinit var candidates: List<Candidate>
    @SerializedName("coins")
    @Expose
    lateinit var coins: List<Coin>

}