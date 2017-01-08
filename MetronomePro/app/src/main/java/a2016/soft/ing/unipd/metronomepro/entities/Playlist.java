package a2016.soft.ing.unipd.metronomepro.entities;

import java.util.List;

/**
 * Created by feder on 09/12/2016.
 */

public interface Playlist extends List<Song> {
    /**
     * Name of the playlist
     * @return string that represents playlist identifier
     */
    String getName();

}
