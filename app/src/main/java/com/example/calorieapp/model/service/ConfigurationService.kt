package com.example.calorieapp.model.service

/**
 * Service interface for fetching configuration settings.
 */
interface ConfigurationService {

    /**
     * Fetches the latest configuration settings.
     * @return True if the configuration was successfully fetched, false otherwise.
     */
    suspend fun fetchConfiguration(): Boolean
    //val isShowTaskEditButtonConfig: Boolean

    // make isShow Water tracking, weight tracking,... or meal tracking later
}
