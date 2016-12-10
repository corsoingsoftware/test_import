package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by feder on 09/12/2016.
 */

public interface DataProvider {
    /**
     * Read all songs
     * @return get all songs in database, in a list format
     */
    List<Song> getSongs();

    /**
     * return a list of songs that respects parameters
     * @param searchName research parameter "like" for name of songs if null=all songs
     * @param playlist to search if null=ignore this parameter
     * @return a list of songs that respects parameters
     */
    List<Song> getSongs(String searchName, Playlist playlist);

    /**
     * read the playlists
     * @param searchName search parameter, ignore if null
     * @return the list of avaiable playlist
     */
    List<Playlist> getPlaylists(String searchName);

    /**
     * memorize the song in database, throw exception if fails
     * override if present!
     * @param song to memorize
     */
    void save(String name, Song song);

    /**
     * Delete the song throw exception if fails
     * @param song to delete
     */
    void deleteSong(Song song);

    /**
     * save the playlist, and the songs in it if they aren't in database already
     * @param playlist to save or update!
     */
    void savePlaylist(Playlist playlist);

    /**
     * delete the playlist but not the songs! Throw exception if fails
     * @param playlist to delete
     */
    void deletePlaylist(Playlist playlist);

}
