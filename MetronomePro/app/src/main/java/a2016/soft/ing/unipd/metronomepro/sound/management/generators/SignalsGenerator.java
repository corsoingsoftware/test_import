package a2016.soft.ing.unipd.metronomepro.sound.management.generators;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Federico Favotto on 08/12/2016.
 */

public class SignalsGenerator {
    /**
     * Audio frequency
     */
    private static final int DEFAULT_SAMPLE_RATE_IN_HERTZ = 8000;
    /**
     * Size of short data
     */
    private static final int SHORT_SIZE=2;

    /**
     * 2 bytes per sample default (short unsigned)
     */
    private static final int DEFAULT_BYTES_PER_SAMPLE = 2;
    /**
     * one channel default
     */
    private static final int DEFAULT_CHANNELS_COUNT = 1;

    /**
     * Sample rate in hertz
     */
    private int sampleRate;
    /**
     * Attualmente non considerato
     *
     */
    private int bytesPerSample;
    /**
     * Attualmente non considerato
     */
    private int channelsCount;


    /**
     * default constructor
     */
    public SignalsGenerator() {
        this(DEFAULT_CHANNELS_COUNT, DEFAULT_SAMPLE_RATE_IN_HERTZ, DEFAULT_BYTES_PER_SAMPLE);
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
    public byte[] generateSin(int lengthInBytes, int sinFrequency){

        byte[] sinusoid = new byte[lengthInBytes];

        for (int i = 0; i < lengthInBytes - 2; i++) {
            double b = Math.sin(2 * Math.PI * i / ( sampleRate / (double)sinFrequency));
            ByteBuffer bb = ByteBuffer.allocate(SHORT_SIZE);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putShort((short) (b * Short.MIN_VALUE));
            bb.position(0);
            sinusoid[i++] = bb.get();
            sinusoid[i] = bb.get();
        }

        return sinusoid;
    }

    /**
     * return an array of byte of silence sound
     * @param lengthInByte
     * @return array of bytes to play
     */
    public byte[] silence(int lengthInByte){

        byte[] toReturn= new byte[lengthInByte];
        ByteBuffer bb = ByteBuffer.allocate(SHORT_SIZE);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(Short.MIN_VALUE);
        bb.position(0);
        byte first=bb.get();
        byte second=bb.get();

        for(int i=0; i < toReturn.length - 2; i++){
            toReturn[i++]=first;
            toReturn[i]=second;
        }

        return toReturn;
    }
}
