package com.ad4th.seoulandroid.api

import com.ad4th.seoulandroid.BuildConfig
import okhttp3.*
import java.io.IOException

class FakeInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var response: Response? = null
        if (BuildConfig.DEBUG) {
            var responseString = ""
            // Get Request URI.
            val uri = chain.request().url().uri()
            // Get Query String.
            val query = uri.toString()
            // Parse the Query String.
            if (query.contains("intro")) {
                responseString = RESPONSE_INTRO
            } else if (query.contains("by")) {
                responseString = RESPONSE_BY
            } else if (query.contains("voting")) {
                responseString = RESPONSE_VOTING
            } else if (query.contains("payment")) {
                responseString = RESPONSE_PAYMENT
            }
            response = Response.Builder()
                    .code(200)
                    .message(responseString)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), responseString.toByteArray()))
                    .addHeader("content-type", "application/json")
                    .build()
        } else {
            response = chain.proceed(chain.request())
        }
        return response
    }

    companion object {
        // FAKE RESPONSES.
        private const val RESPONSE_INTRO = "{\"id\":1,\"age\":28,\"name\":\"Victor Apoyan\"}"
        private const val RESPONSE_BY = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}"
        private const val RESPONSE_VOTING = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}"
        private const val RESPONSE_PAYMENT = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}"
    }
}