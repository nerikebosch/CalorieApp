package com.example.calorieapp.model.service.impl

import com.example.calorieapp.model.service.LogService
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import javax.inject.Inject


class LogServiceImpl @Inject constructor() : LogService {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}
