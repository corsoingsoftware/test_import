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
     * Configurazione del canale audio
     * Config for Audiotrack
     */
    private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    /**
     * Configurazione del formato: pcm è un formato non compresso, anche i wav han lo stesso coding
     * Config format for audiotrack
     */
    private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * default constructor
     */
    public SignalsGenerator() {
        this(DEFAULT_SAMPLE_RATE_IN_HERTZ, DEFAULT_AUDIO_FORMAT, DEFAULT_CHANNEL_CONFIG);
    }

    /**
     * Constructor to initialize the generator
     * @param sampleRate sample rate in hertz
     * @param audioFormat format of audio, use {@link AudioFormat} class
     * @param channelConfig config of channels, use {@link AudioFormat} class
     */
    public SignalsGenerator(long sampleRate,
                            int audioFormat,
                            int channelConfig){

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
