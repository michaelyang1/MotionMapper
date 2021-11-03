package com.example.motionmapper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import android.content.ComponentName
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream


class PhoneReceiver : BroadcastReceiver() {
    /**
     * Function to receive incoming phone calls from app background
     */
    override fun onReceive(p0: Context?, p1: Intent?) {
        // save state of phone call
        val state = p1?.getStringExtra(TelephonyManager.EXTRA_STATE)

        // initialize sensor objects
        var sensorManager = p0?.getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val context = p0 // save context for later use in writing output file
        val MAX_COUNT = 100 // data collection duration = MAX_COUNT * sensor sampling rate
        var count = 0
        val motions = mutableListOf<List<Float>>() // list of all samples collected
        val sensorEventListener = object: SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {
                if (count < MAX_COUNT) {
                    // collect data specifically for accelerometer
                    if (p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
                        val ax = p0.values[0] // x acceleration
                        val ay = p0.values[1] // y acceleration
                        val az = p0.values[2] // z acceleration

                        // append motion locations
                        motions.add(listOf(ax, ay, az))
                        ++count
                        Log.e("PHONE RECEIVER", "$ax, $ay, $az")

                        // if phone is picked up, write to file
                        if (count == MAX_COUNT) {
                            val finalMotion = motions[motions.size - 1]
                            val ax_f = finalMotion[0]
                            val ay_f = finalMotion[1]
                            val az_f = finalMotion[2]
                            Log.e("PHONE RECEIVER", "Data collection is complete")
                            // if device is still flat, no need to write to file
                            if (isFlat(ax_f.toDouble(), ay_f.toDouble(), az_f.toDouble())) {
                                return
                            }

                            // write to file
                            val myExternalFile = File(context.getExternalFilesDir("MotionsDirectory"),
                                "motions.txt")
                            val fos = FileOutputStream(myExternalFile)
                            motions.forEach {
                                fos.write("${it[0]},${it[1]},${it[2]}\n".toByteArray())
                            }
                            fos.close()
                        }
                    }
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
                TODO("Not yet implemented")
            }

        }

        // register sensor listener
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        if (state == TelephonyManager.EXTRA_STATE_RINGING) { // phone is ringing
            Log.e("PHONE RECEIVER", "INCOMING CALL CURRENTLY")
        } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) { // call picked up
            sensorManager.unregisterListener(sensorEventListener)
        } else if (state == TelephonyManager.EXTRA_STATE_IDLE) { // call ended
            sensorManager.unregisterListener(sensorEventListener)
        }
    }

    /**
     * Function to determine if phone is lying flat or not (with certain tolerance)
     */
    private fun isFlat(x: Double, y: Double, z: Double): Boolean {
        val norm = Math.sqrt(x * x + y * y + z * z)

        // normalize z
        val z_norm = z / norm

        // calculate tilt (inclination)
        val inclination = Math.round(Math.toDegrees(Math.acos(z_norm))).toInt()
        Log.e("PHONE RECEIVER", "Inclination: $inclination")

        // check if device is within threshold of being flat (tolerance values)
        if (inclination < 25 || inclination > 155) { // within (flat)
            return true
        }
        return false
    }
}