package com.example.calorieapp.api

import com.example.calorieapp.model.OpenFoodFactsResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface defining API endpoints for interacting with the OpenFoodFacts database.
 * Uses Retrofit for making network requests.
 */
interface OpenFoodFactsApi {
    /**
     * Searches for products in the OpenFoodFacts database.
     *
     * @param searchTerms The search query string to find relevant products.
     * @param pageSize The number of results to return per page (default is 10).
     * @param json Ensures that the response is returned in JSON format (default is 1).
     * @return An instance of [OpenFoodFactsResponse] containing the search results.
     */
    @GET("/cgi/search.pl")
    suspend fun searchProducts(
        @Query("search_terms") searchTerms: String,
        @Query("page_size") pageSize: Int = 10,
        @Query("json") json: Int = 1,
        //@Query("json") json: Boolean = true,
        // @Query("fields") fields: String = "product_name,nutriments"
    ): OpenFoodFactsResponse
}