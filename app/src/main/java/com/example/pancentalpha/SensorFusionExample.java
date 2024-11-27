package com.example.pancentalpha;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorFusionExample implements SensorEventListener {

    // Sensor variables
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    // Orientation angles
    private float pitch;
    private float roll;

    // Filter constant
    private static final float ALPHA = 0.98f;

    // Timestamp for calculating time difference
    private long lastTimestamp = 0;

    // Conversion factor for nanoseconds to seconds
    private static final float NS2S = 1.0f / 1000000000.0f;

    public SensorFusionExample(Context context) {
        // Initialize sensor manager and sensors
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // Register sensor listeners
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Get sensor type
        int sensorType = event.sensor.getType();

        // Handle accelerometer data
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            // Get accelerometer values
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculate pitch and roll from accelerometer data
            pitch = (float) Math.atan2(y, z);
            roll = (float) Math.atan2(-x, Math.sqrt(y * y + z * z));
        }

        // Handle gyroscope data
        else if (sensorType == Sensor.TYPE_GYROSCOPE) {
            // Get gyroscope values
            float gyroX = event.values[0];
            float gyroY = event.values[1];
            float gyroZ = event.values[2];

            // Calculate time difference
            float dt = (event.timestamp - lastTimestamp) * NS2S;

            // Update orientation angles using complementary filter
            pitch = ALPHA * (pitch + gyroY * dt) + (1 - ALPHA) * pitch;
            roll = ALPHA * (roll + gyroX * dt) + (1 - ALPHA) * roll;

            // Update last timestamp
            lastTimestamp = event.timestamp;
        }

        // ... (Calculate distance based on orientation and acceleration)
        // Variables for distancecalculation
        float[] gravity = new float[3];
        float[] linearAcceleration = new float[3];
        float[] displacement = new float[2]; // x, y displacement
        float distance = 0;



        // Calculate gravity component
        final float alpha = 0.8f; // Gravity filter constant
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Subtract gravity to get linear acceleration
        linearAcceleration[0] = event.values[0] - gravity[0];
        linearAcceleration[1] = event.values[1] - gravity[1];
        linearAcceleration[2] = event.values[2] - gravity[2];

        // Project acceleration onto horizontal plane
        float projectedX = (float) (linearAcceleration[0] * Math.cos(pitch) + linearAcceleration[2] * Math.sin(pitch));
        float projectedY = (float) (linearAcceleration[1] * Math.cos(roll) + linearAcceleration[2] * Math.sin(roll));

        // Double integration to estimate displacement
        float dt = (event.timestamp - lastTimestamp) * NS2S;
        displacement[0] += projectedX * dt * dt;
        displacement[1] += projectedY * dt * dt;
        // Calculate distance
        distance = (float) Math.sqrt(displacement[0] * displacement[0] + displacement[1] * displacement[1]);
        // ... (Use distance value)

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    // ... (Other methods for distance calculation and data processing)
}

