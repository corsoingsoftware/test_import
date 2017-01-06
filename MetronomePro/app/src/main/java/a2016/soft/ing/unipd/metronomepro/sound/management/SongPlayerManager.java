package a2016.soft.ing.unipd.metronomepro.sound.management;

/**
 * Created by feder on 22/12/2016.
 */

public interface SongPlayerManager {
    /**
     * get the instantiated song player for midis
     * @return the {@link SongPlayer}
     */
    SongPlayer getMidiSongPlayer();

    /**
     * get the instantiated song player for timeslices
     * @return the {@link SongPlayer}
     */
    SongPlayer getTimeSlicesSongPlayer();
}
