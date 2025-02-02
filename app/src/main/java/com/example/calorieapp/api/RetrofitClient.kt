package com.example.calorieapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton object that provides a Retrofit client instance for accessing the OpenFoodFacts API.
 */
object RetrofitClient {
    /**
     * Base URL for the OpenFoodFacts API.
     */
    private const val BASE_URL = "https://world.openfoodfacts.org"

    /**
     * Logging interceptor for monitoring network requests and responses.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * OkHttpClient instance with logging and custom headers.
     *
     * - Adds a logging interceptor to log request and response details.
     * - Adds a User-Agent header required by the OpenFoodFacts API.
     * - Sets connection and read timeouts to 15 seconds.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            // Add User-Agent header as required by OpenFoodFacts API
            val original = chain.request()
            val request = original.newBuilder()
                .header("User-Agent", "CalorieApp - Android - Version 1.0")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Lazily initialized Retrofit API instance for making network requests.
     *
     * @return An implementation of [OpenFoodFactsApi] for accessing the API endpoints.
     */
    val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }
}
