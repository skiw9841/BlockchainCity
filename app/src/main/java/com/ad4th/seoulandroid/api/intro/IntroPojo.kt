package com.ad4th.seoulandroid.api.intro

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IntroPojo {
    @SerializedName("votings")
    @Expose
    lateinit var votings: List<Voting>
    @SerializedName("coins")
    @Expose
    lateinit var coins: List<Coin_>

}

// votings:[]