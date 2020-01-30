package com.ad4th.seoulandroid.api

import com.ad4th.seoulandroid.BuildConfig
import com.ad4th.seoulandroid.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

object APIClient {
    private lateinit var retrofit: Retrofit
    private var mockRetrofit: MockRetrofit? = null
    val client: Retrofit
        get() {
            val client: OkHttpClient
            if (BuildConfig.DEBUG) {
                val fakeInterceptor = FakeInterceptor()
                client = OkHttpClient.Builder().addInterceptor(fakeInterceptor).build()
            } else {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            }
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.EVOTING_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            val behavior = NetworkBehavior.create()
            mockRetrofit = MockRetrofit.Builder(retrofit)
                    .networkBehavior(behavior)
                    .build()
            return retrofit
        }
}