package a2016.soft.ing.unipd.metronomepro.sound.management;

import android.media.AudioFormat;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by feder on 08/12/2016.
 */


public interface SongPlayer {

    static final int SAMPLE_RATE_IN_HERTZ = 8000;
    static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    static final int FRAME_SIZE = 2;
    static final int SECS_IN_MIN = 60;

    /**
     * Frequenza della sinusoide
     * sin frequency of beep
     */
    static final int DEFAULT_BEEP_FREQUENCY=610;

    /**
     * Frequenza della sinusoide
     * sin frequency OF boop
     */
    static final int DEFAULT_BOOP_FREQUENCY=400;
    /**
     * Lunghezza totale della sinusoide in bytes questa lunghezza deve essere molto più breve dei max bpm, che attualmente son 300
     * per decisione di struttura
     * Total length in bytes of the "beep", it must be shorter than minimum period, so when bpm are higher
     */
    static final int DEFAULT_SIN_LENGTH_IN_BYTES=500;

    /**
     * Start the current song
     */
    void play();

    /**
     * Initialize the player with all parameter
     * @param frequencyBeep frequency of higher tone beep
     * @param lengthBeep length of higher tone beep
     * @param frequencyBoop frequency of lower tone boop
     * @param lengthBoop length of lower tone boop
     * @param sampleRate rate of audio signal
     * @param audioFormat format of audio signal
     * @param channelConfig config of channels
     */
    void initialize(int frequencyBeep,
                    int lengthBeep,
                    int frequencyBoop,
                    int lengthBoop,
                    int sampleRate,
                    int audioFormat,
                    int channelConfig);

    /**
     * Initialize the player with only some parameter
     * @param sampleRate rate of audio signal
     * @param audioFormat format of audio signal
     * @param channelConfig config of channels
     */
    void initialize(int sampleRate,
                    int audioFormat,
                    int channelConfig);


    /**
     * Initialize the default player
     */
    void initialize();


    /**
     * Pause the current song
     */
    void pause();

    /**
     * Stop the current song
     */
    void stop();

    /**
     * load the song into the current playlist.
     * @param song
     */
    void load(Song song);

    /**
     * Return the song
     * @param s song to search
     * @return the bytearray to describe song in current format
     */
    byte[] getSong(Song s);

    /**
     * State of current song
     * @return PLaystate that describes state
     */
    PlayState getState();
}
