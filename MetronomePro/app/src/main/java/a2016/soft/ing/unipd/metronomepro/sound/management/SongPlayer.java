package a2016.soft.ing.unipd.metronomepro.sound.management;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by feder on 08/12/2016.
 */

public interface SongPlayer {
    /**
     * Start the current song
     */
    void play();

    /**
     * Initialize the player with all parameter
     * @param frequencyBeep frequency of higher tone beep
     * @param lenghtBeep length of higher tone beep
     * @param frequencyBoop frequency of lower tone boop
     * @param lenghtBoop length of lower tone boop
     * @param sampleRate rate of audio signal
     * @param audioFormat format of audio signal
     * @param channelConfig config of channels
     */
    void initialize(long frequencyBeep,
                    long lenghtBeep,
                    long frequencyBoop,
                    long lenghtBoop,
                    long sampleRate,
                    int audioFormat,
                    int channelConfig);

    /**
     * Initialize the player with only some parameter
     * @param sampleRate rate of audio signal
     * @param audioFormat format of audio signal
     * @param channelConfig config of channels
     */
    void initialize(long sampleRate,
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