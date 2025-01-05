package com.example.calorieapp.model.service

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean
    //val isShowTaskEditButtonConfig: Boolean

    // make isShow Water tracking, weight tracking,... or meal tracking later
}
