package com.example.calorieapp.api

import com.example.calorieapp.model.OpenFoodFactsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFoodFactsApi {
    @GET("/cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") searchTerms: String,
        @Query("page_size") pageSize: Int = 10,
        @Query("json") json: Int = 1,
        //@Query("json") json: Boolean = true,
        // @Query("fields") fields: String = "product_name,nutriments"
    ): OpenFoodFactsResponse
}