package com.ad4th.seoulandroid.api.intro

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

class Coin {
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("symbol")
    @Expose
    var symbol: String? = null
    @SerializedName("decimals")
    @Expose
    var decimals: BigInteger? = null
    @SerializedName("initialSupply")
    @Expose
    var initialSupply: BigInteger? = null
    @SerializedName("contractAddress")
    @Expose
    var contractAddress: String? = null
    @SerializedName("coinType")
    @Expose
    var coinType: String? = null
    @SerializedName("administrator")
    @Expose
    var administrator: String? = null
    @SerializedName("transferAmount")
    @Expose
    var transferAmount: BigInteger? = null
    @SerializedName("sequence")
    @Expose
    var sequence: Int? = null
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