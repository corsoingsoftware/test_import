package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;

import a2016.soft.ing.unipd.metronomepro.data.access.layer.SQLiteDataProvider;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;


/**
 * Created by Francesco on 10/12/2016.
 */

public class SQLiteDataProviderTest{
    Context c;

    /**
     * test per la creazione del DB
     */
    @Test
    public void onCreateTest(){

        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);

    }

    /**
     * test per il salvtaggio di una traccia
     * crea nuovo db
     * inizializza song e il suo nome
     * li salva sul db
     * uso il get per vedere se sono stati effetivamente salvati
     */
    @Test
    public void saveTrackTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        Song s = EntitiesBuilder.getSong();
        dataProvider.save(s);
        dataProvider.getSongs(s.getName(),null);
    }

    /**
     * test per l'eliminazione di una song
     * salva la song come sopra
     * la elimina
     * uso il get per vedere se è stata effetivamente eliminata
     */
    @Test
    public void deleteTrackTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        Song s = EntitiesBuilder.getSong();
        dataProvider.save(s);
        dataProvider.deleteSong(s);
    }

    /**
     * test per il salvtaggio di una playlist
     * crea nuovo db
     * inizializza la playlist
     * la salva sul db
     * uso il get per vedere se è stata effetivamente salvati
     */
    @Test
    public void savePlaylistTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        String playlistName = "Test playlist";
        Playlist p = EntitiesBuilder.getPlaylist(playlistName);
        dataProvider.savePlaylist(p);
        dataProvider.getPlaylists(playlistName);
    }

    /**
     * test per l'eliminazione di una playlist
     * salva la playlist come sopra
     * la elimina
     * uso il get per vedere se è stata effetivamente eliminata
     */
    @Test
    public void deletePlaylistTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        String playlistName = "Test playlist";
        Playlist p = EntitiesBuilder.getPlaylist(playlistName);
        dataProvider.savePlaylist(p);
        dataProvider.deletePlaylist(p);
        dataProvider.getPlaylists(playlistName);
    }

}
