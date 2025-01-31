package com.example.calorieapp.model.service

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class StepDetector(
    private val context: Context,
    private val onStepDetected: (Int) -> Unit
) {
    private val sensorManager: SensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val stepSensor: Sensor? by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private var initialStepCount = 0
    private var currentStepCount = 0

    private val stepSensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if (initialStepCount == 0) {
                initialStepCount = event.values[0].toInt()
            }

            currentStepCount = event.values[0].toInt() - initialStepCount
            onStepDetected(currentStepCount)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Handle sensor accuracy changes if needed
        }
    }

    fun start() {
        stepSensor?.let {
            sensorManager.registerListener(
                stepSensorListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(stepSensorListener)
    }

    fun resetSteps() {
        initialStepCount = 0
        currentStepCount = 0
    }

    fun getCurrentSteps(): Int = currentStepCount
}