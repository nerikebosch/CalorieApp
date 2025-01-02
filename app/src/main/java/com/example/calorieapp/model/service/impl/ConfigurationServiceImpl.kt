package com.example.calorieapp.model.service.impl

import com.example.calorieapp.BuildConfig
import com.example.calorieapp.model.service.ConfigurationService
import com.google.firebase.Firebase
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.example.calorieapp.R.xml as AppConfig

class ConfigurationServiceImpl @Inject constructor() : ConfigurationService {
    private val remoteConfig
        get() = Firebase.remoteConfig

    init {
        if (BuildConfig.DEBUG) {
            val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 0 }
            remoteConfig.setConfigSettingsAsync(configSettings)
        }

        remoteConfig.setDefaultsAsync(AppConfig.remote_config_defaults)
    }

    override suspend fun fetchConfiguration(): Boolean {
        val trace = FirebasePerformance.getInstance().newTrace(FETCH_CONFIG_TRACE)
        return try {
            trace.start()
            val result = remoteConfig.fetchAndActivate().await()
            trace.stop()
            result
        } catch (e: Exception) {
            trace.stop()
            false
        }
    }

//    override val isShowTaskEditButtonConfig: Boolean
//        get() = remoteConfig[SHOW_TASK_EDIT_BUTTON_KEY].asBoolean()

    companion object {
        private const val SHOW_TASK_EDIT_BUTTON_KEY = "show_task_edit_button"
        private const val FETCH_CONFIG_TRACE = "fetchConfig"
    }
}
