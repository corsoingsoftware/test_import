package a2016.soft.ing.unipd.metronomepro.sound.management;

import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by feder on 08/12/2016.
 * This class uses AudioTrack to create song in bytes from a Song, and provides methods to play the track!
 */

public class AudioTrackSongPlayer implements SongPlayer {

    @Override
    public void play() {

    }

    @Override
    public void initialize(long frequencyBeep, long lenghtBeep, long frequencyBoop, long lenghtBoop, long sampleRate, int audioFormat, int channelConfig) {

    }

    @Override
    public void initialize(long sampleRate, int audioFormat, int channelConfig) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void load(Song song) {

    }

    @Override
    public byte[] getSong(Song s) {
        return new byte[0];
    }

    @Override
    public PlayState getState() {
        return null;
    }
}
