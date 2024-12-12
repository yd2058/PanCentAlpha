package com.example.pancentalpha;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;


import androidx.core.content.ContextCompat;

public class LiveDbMeter {

    private static final int SAMPLE_RATE = 44100; // Hz
    private static final int BUFFER_SIZE = 4096; // Samples
    private static final double BANDWIDTH = 100; // Hz (Adjust as needed)

    private AudioRecord audioRecord;
    private boolean isRecording;

    double hicutfreq, locutfreq;

    public LiveDbMeter(Context context) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED) {

            audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE * 2
        );}


    }

    public void start(double tfrequency) {
        isRecording = true;
        audioRecord.startRecording();

        hicutfreq = tfrequency + BANDWIDTH / 2;
        locutfreq = tfrequency - BANDWIDTH / 2;




        new Thread(() -> {
            while (isRecording) {
                short[] buffer = new short[BUFFER_SIZE];
                audioRecord.read(buffer, 0, BUFFER_SIZE);

                // Convert to double array for filtering
                double[] audioData = new double[BUFFER_SIZE];
                for (int i = 0; i < BUFFER_SIZE; i++) {
                    audioData[i] = buffer[i];
                }

                // Apply the goertzel algorythm as frequency isolator
                double magnitude = goertzel(audioData, tfrequency, SAMPLE_RATE);

                // Calculate dB level
                double db = 20 * Math.log10(magnitude/32767.0);


                Decimet.getInstance().updateDB(db);
            }
        }).start();
    }

    public void stop() {
        isRecording = false;
        audioRecord.stop();
        audioRecord.release();
    }



    private double goertzel(double[] data, double targetFrequency, double sampleRate) {
        int k = (int) (0.5 + ((data.length * targetFrequency) / sampleRate));
        double omega = (2.0 * Math.PI * k) / data.length;
        double cosine = Math.cos(omega);
        double sine = Math.sin(omega);
        double coeff = 2.0 * cosine;

        double q0 = 0;
        double q1 = 0;
        double q2 = 0;

        for (int i = 0; i < data.length; i++) {
            q0 = coeff * q1 - q2 + data[i];
            q2 = q1;
            q1 = q0;
        }

        double real = (q1 - q2 * cosine);
        double imag = (q2 * sine);
        double magnitude = Math.sqrt(real * real + imag * imag);

        return magnitude;
    }
}