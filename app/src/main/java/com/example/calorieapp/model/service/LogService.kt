package com.example.calorieapp.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}