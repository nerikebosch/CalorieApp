package com.example.calorieapp.screens.adddata

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodFactsApi {
    @GET("cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") searchTerms: String,
        @Query("page_size") pageSize: Int = 10,
        @Query("json") json: Boolean = true
    ): OpenFoodFactsResponse
}