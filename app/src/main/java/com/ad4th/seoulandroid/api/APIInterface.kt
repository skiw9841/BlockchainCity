package com.ad4th.seoulandroid.api

import com.ad4th.seoulandroid.api.intro.IntroPojo
import com.ad4th.seoulandroid.api.user.UserPojo
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {
    @GET("intro")
    fun doGetIntro(): Call<IntroPojo?>

    @GET("users/by/{address}")
    fun doGetUser(@Path(value = "address", encoded = true) address: String?): Call<UserPojo?>

    /*
{
"votingId": 1,
"candidateId": 1,
"userId": 2,
"txId": "aaaaa"
}
 */
    @POST("users/voting")
    fun doPostVote(@Body body: RequestBody?): Call<Void?>

    /*
    {
        "userId": 0,
            "coinId": 0,
            "txId": "string"
    }
     */
    @POST("users/payment")
    fun doPostPay(@Body body: RequestBody?): Call<Void?>
}