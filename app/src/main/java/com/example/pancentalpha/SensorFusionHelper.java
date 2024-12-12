package com.example.pancentalpha;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorFusionHelper implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer, gyroscope;
    private float[] gravity = new float[3];
    private float[] linearAcceleration = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientation = new float[3];
    private float[] velocity = new float[3]; // Estimated velocity from accelerometer data
    private float[] position = new float[3]; // Estimated position in x, y, z
    private long lastUpdateTime;
    float alpha;


    // Constructor that initializes the sensor fusion helper
    public SensorFusionHelper(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        lastUpdateTime = System.currentTimeMillis();
    }

    // Method to start listening to sensors
    public void start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_UI);
    }

    // Method to stop listening to sensors
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Obtain raw accelerometer data
            float[] accelData = event.values;

            // Low-pass filter for gravity
            alpha = 0.3f;// The smoothing factor (you can adjust this)
            gravity[0] = alpha * gravity[0] + (1 - alpha) * accelData[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * accelData[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * accelData[2];

            // Calculate linear acceleration by subtracting gravity from the accelerometer readings
            linearAcceleration[0] = accelData[0] - gravity[0];
            linearAcceleration[1] = accelData[1] - gravity[1];
            linearAcceleration[2] = accelData[2] - gravity[2];
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            // Use gyroscope data for orientation estimation (for sensor fusion)
            SensorManager.getRotationMatrix
                    (rotationMatrix, null, gravity, event.values);
            SensorManager.getOrientation(rotationMatrix, orientation);
        }

        // Update position and velocity based on accelerometer and gyroscope readings
        updatePosition(event.timestamp);
    }

    private void updatePosition(long timestamp) {
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f; // Convert to seconds

        // Update velocity using linear acceleration data (integrating over time)
        velocity[0] += linearAcceleration[0] * deltaTime;
        velocity[1] += linearAcceleration[1] * deltaTime;
        velocity[2] += linearAcceleration[2] * deltaTime;

        // Update position using velocity (integrating over time)
        position[0] += velocity[0] * deltaTime;
        position[1] += velocity[1] * deltaTime;
        position[2] += velocity[2] * deltaTime;

        lastUpdateTime = currentTime;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle sensor accuracy changes if needed
    }

    // Utility method to compute the distance between two 3D points (in meters)
    public float distanceBetweenCoordinates(float[] pos1, float[] pos2) {
        return (float) Math.sqrt(Math.pow(pos2[0] - pos1[0], 2) + Math.pow(pos2[1] - pos1[1], 2) + Math.pow(pos2[2] - pos1[2], 2));
    }

    public  double angleBetweenCoordinates(float[] first, float[] middle, float[] ending) {
        // Calculate vectors from middle point to first and ending points
        double[] v1 = {first[0] - middle[0], first[1] - middle[1], first[2] - middle[2]};
        double[] v2 = {ending[0] - middle[0], ending[1] - middle[1], ending[2] - middle[2]};

        // Calculate dot product of the two vectors
        double dotProduct = v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];

        // Calculate magnitudes of the two vectors
        double magnitudeV1 = Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1] + v1[2] * v1[2]);
        double magnitudeV2 = Math.sqrt(v2[0] * v2[0] + v2[1] * v2[1] + v2[2] * v2[2]);

        // Calculate the angle in radians using arccosine
        double angleRadians = Math.acos(dotProduct / (magnitudeV1 * magnitudeV2));

        // Convert radians to degrees (optional)
        double angleDegrees = Math.toDegrees(angleRadians);

        return angleDegrees; // Or return angleRadians if you prefer radians
    }

    // Get the current position as an array of x, y, z coordinates
    public float[] getCurrentPosition() {
        return position;
    }

    // Get the current velocity as an array of x, y, z velocities
    public float[] getCurrentVelocity() {
        return velocity;
    }

    public float getalpha(){
        return alpha;
    }
    public void setalpha(float i){
        alpha = i;
    }
}
