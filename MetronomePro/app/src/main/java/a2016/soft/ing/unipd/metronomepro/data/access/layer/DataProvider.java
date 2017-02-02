package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Federico Favotto on 09/12/2016.
 */

public interface DataProvider {
    /**
     * Read all the stored songs
     * @return as a list, all songs in the database
     */
    List<Song> getAllSongs();

    /**
     * Search the song requested
     * @param searchName is the name of the song, it can't be null
     * @return the searched songs, null if it doesn't find it
     */
    Song getSong(String searchName);

    /**
     * Search the playlist requested
     * @param searchName is the name of the playlist, it can't be null
     * @return the searched playlist, null if it doesn't find it
     */
    Playlist getPlaylist(String searchName);

    /**
     * Get all the name of the avaible playlists
     * @return a list of String about avaiable playlists
     */
    List<String> getAllPlaylists();

    /**
     * Memorize the song in database, throw exception if fails
     * Added return parameter to know if the save was successful, by Munerato
     * @param song to memorize
     * @return boolean, true if the save was successful
     */
    boolean saveSong(Song song);

    /**
     * Delete the song throw exception if fails
     * Added return parameter to know if the save was successful, by Munerato
     * @param song to delete
     * @return boolean
     */
    boolean deleteSong(Song song);

    /**
     * save the playlist, and the songs in it if they aren't in database already
     * @param playlist to save or update!
     * @return boolean, true if the save was successful
     */
    boolean savePlaylist(Playlist playlist);

    /**
     * delete the playlist but not the songs!
     * Added return parameter to know if the save was successful, by Munerato
     * @param playlist to delete
     * @return boolean, true if the save was successful
     *
     */
    boolean deletePlaylist(Playlist playlist);

    /**
     * replace an old song into a new one
     * @param oldSong to replace
     * @param newSong to insert
     * @return boolean, true if the replace was successful
     */
    boolean modifySong(Song oldSong, Song newSong);

    /**
     * replace an old playlist into a new one
     * @param oldPlaylist to replace
     * @param newPlaylist to insert
     * @return boolean, true if the replace was successful
     */
    boolean modifyPlaylist(Playlist oldPlaylist, Playlist newPlaylist);
}
