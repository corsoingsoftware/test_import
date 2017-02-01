package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import a2016.soft.ing.unipd.metronomepro.data.access.layer.SQLiteDataProvider;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;


/**
 * Created by Francesco on 10/12/2016.
 */

@RunWith(AndroidJUnit4.class)
public class SQLiteDataProviderTest {
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
        public void saveTimeSlicesSongTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        Song songToAddforTest = EntitiesBuilder.getTimeSlicesSong();
        dataProvider.save(songToAddforTest);
        dataProvider.getSongs(songToAddforTest.getName(),null);
    }

    @Test
    public void saveMidiSongTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        Song songToAddforTest = EntitiesBuilder.getMidiSong();
        dataProvider.save(songToAddforTest);
        dataProvider.getSongs(songToAddforTest.getName(),null);
    }

    /**
     * test per l'eliminazione di una song
     * salva la song come sopra
     * la elimina
     * uso il get per vedere se è stata effetivamente eliminata
     */
    @Test
    public void deleteTimeSlicesSongTest(){
        SQLiteDataProvider dataProvider = new SQLiteDataProvider(c);
        Song songToDeleteforTest = EntitiesBuilder.getTimeSlicesSong();
        songToDeleteforTest.setName("SongtoDelete");
        dataProvider.save(songToDeleteforTest);
        dataProvider.deleteSong(songToDeleteforTest);
        dataProvider.getSongs(songToDeleteforTest.getName(), null);
        //TODO prendi l'ecezione se non trova la song
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
        String playlistName = "TestPlaylist";
        Playlist playlistToAddforTest = EntitiesBuilder.getPlaylist(playlistName);
        dataProvider.savePlaylist(playlistToAddforTest);
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
        //TODO prendi l'eccezione per playlist non trovato anche qui
    }

}
