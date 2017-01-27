package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Federico Favotto on 09/12/2016.
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
