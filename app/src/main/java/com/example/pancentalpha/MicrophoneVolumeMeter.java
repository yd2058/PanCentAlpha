package com.example.pancentalpha;

import android.media.AudioFormat;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;

public class MicrophoneVolumeMeter {

    public double getvol() {
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        TargetDataLine targetLine = AudioSystem.getTargetDataLine(format);
        targetLine.open(format);
        targetLine.start();

        byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = targetLine.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                double volume = calculateVolume(buffer);
                System.out.println("Volume: " + volume);
                // Update UI or perform actions based on volume
            }
        }
    }

    private static double calculateVolume(byte[] buffer) {
        double sum = 0.0;
        for (byte b : buffer) {
            sum += Math.pow(b, 2);
        }
        double rms = Math.sqrt(sum / buffer.length);
        return rms;
    }
}
