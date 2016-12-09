package a2016.soft.ing.unipd.metronomepro.sound.management.generators;

import android.media.AudioFormat;

/**
 * Created by feder on 08/12/2016.
 */

public class SignalsGenerator {
    /**
     * Audio frequency
     */
    private static final int DEFAULT_SAMPLE_RATE_IN_HERTZ = 8000;

    /**
     * 2 bytes per sample default (short unsigned)
     */
    private static final int DEFAULT_BYTES_PER_SAMPLE = 2;
    /**
     * one channel default
     */
    private static final int DEFAULT_CHANNELS_COUNT = 1;

    private int sampleRate;
    private int bytesPerSample;
    private int channelsCount;


    /**
     * default constructor
     */
    public SignalsGenerator() {
        this(DEFAULT_SAMPLE_RATE_IN_HERTZ, DEFAULT_BYTES_PER_SAMPLE, DEFAULT_CHANNELS_COUNT);
    }

    /**
     * Constructor to initialize the generator
     * @param sampleRate sample rate in hertz
     * @param bytesPerSample bytes per sample
     * @param channelsCount number of channels, it defines the frame size
     */
    public SignalsGenerator(int channelsCount, int sampleRate, int bytesPerSample) {
        this.channelsCount = channelsCount;
        this.sampleRate = sampleRate;
        this.bytesPerSample = bytesPerSample;
    }

    /**
     * generates a sinusoidal sound
     * @param lengthInBytes lenght in bytes of sinus
     * @return sinus sound in bytes to play it with current configs
     */
    public byte[] generateSin(long lengthInBytes){
        return null;
    }

    /**
     * return an array of byte of silence sound
     * @param lengthInByte
     * @return array of bytes to play
     */
    public byte[] silence(long lengthInByte){
        return null;
    }
}