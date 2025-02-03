package com.example.calorieapp.model.service

/**
 * Service interface for logging errors and crashes.
 */
interface LogService {

    /**
     * Logs a non-fatal crash.
     * @param throwable The exception to log.
     */
    fun logNonFatalCrash(throwable: Throwable)
}