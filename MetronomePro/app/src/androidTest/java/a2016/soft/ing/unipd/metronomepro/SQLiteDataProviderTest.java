package a2016.soft.ing.unipd.metronomepro;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderConstants;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;


/**
 * Created by Francesco on 10/12/2016.
 * Edited by Munerato.
 */

@RunWith(AndroidJUnit4.class)
public class SQLiteDataProviderTest {
    Context context;
    private DataProvider database;

    /**
     * Creazione del database
     */
    @Before
    public void setUp() throws Exception {
        context = getTargetContext();
        context.deleteDatabase(DataProviderConstants.DB_NAME);
        database = DataProviderBuilder.getDefaultDataProvider(context);
    }

    /**
     * Test for saving a song
     */
    @Test
        public void saveTrackTest(){
        MidiSong createdMidiSong = createMidi();
        boolean midiSaved = database.saveSong(createdMidiSong);
        assertTrue("The song is not saved", midiSaved);
        TimeSlicesSong createdTSS = createTS();
        boolean tssSaved = database.saveSong(createdTSS);
        assertTrue("The song wasn't saved", tssSaved);
    }

    /**
     * Test for deleting a song
     */
    @Test
    public void deleteTrackTest(){
        MidiSong createdMidiSong = createMidi();
        database.saveSong(createdMidiSong);
        boolean songDeleted = database.deleteSong(createdMidiSong);
        assertTrue("The song still present on db", songDeleted);
    }

    /**
     * Test for saving a playlist (needs a song to be saved)
     */
    @Test
    public void savePlaylistTest(){
        Playlist createdPlaylist = EntitiesBuilder.getPlaylist("playlistTest");
        MidiSong createdMidiSong = createMidi();
        database.saveSong(createdMidiSong);
        createdPlaylist.add(createdMidiSong);
        boolean playlistSaved = database.savePlaylist(createdPlaylist);
        assertTrue("The playlist wasn't saved ", playlistSaved);
    }

    /**
     * Test for deleting a playlist
     */
    @Test
    public void deletePlaylistTest(){
        Playlist createdPlaylist = EntitiesBuilder.getPlaylist("playlistTest");
        MidiSong createdMidiSong = createMidi();
        database.saveSong(createdMidiSong);
        createdPlaylist.add(createdMidiSong);
        database.savePlaylist(createdPlaylist);
        boolean playlistDeleted = database.deletePlaylist(createdPlaylist);
        assertTrue("The playlist wasn't deleted ", playlistDeleted);
    }

    /**
     * Test to modifying a song into another one (even if they are of a different type)
     */
    @Test
    public void modifySongTest(){
        MidiSong createdMidiSong = createMidi();
        database.saveSong(createdMidiSong);
        TimeSlicesSong createdTSS = createTS();
        boolean songModified = database.modifySong(createdMidiSong, createdTSS);
        assertTrue("The song wasn't modified", songModified);

    }

    /**
     * Test to modifying a playlist into another one
     */
    @Test
    public void modifyPlaylist(){
        MidiSong midiSong = createMidi();
        database.saveSong(midiSong);
        TimeSlicesSong tsSong = createTS();
        database.saveSong(tsSong);
        Playlist p1 = EntitiesBuilder.getPlaylist("Playlist1");
        Playlist p2 = EntitiesBuilder.getPlaylist("Playlist2");
        p1.add(midiSong);
        p1.add(tsSong);
        p2.add(tsSong);
        database.savePlaylist(p1);
        boolean modifiedPlaylist = database.modifyPlaylist(p1, p2);
        assertTrue("The playlist wasn't modified", modifiedPlaylist);
    }

    /**
     * Test to get all songs
     */
    @Test
    public void getAllSongsTest(){
        MidiSong midiSong = createMidi();
        database.saveSong(midiSong);
        TimeSlicesSong tsSong = createTS();
        database.saveSong(tsSong);
        List<Song> allSongs = database.getAllSongs();
        assertEquals("The number of elements are different", allSongs.size(), 2);
    }

    /**
     * Test to get all playlists
     */
    @Test
    public void getAllPlaylistsTest(){
        MidiSong midiSong = createMidi();
        database.saveSong(midiSong);
        TimeSlicesSong tsSong = createTS();
        database.saveSong(tsSong);
        Playlist p1 = EntitiesBuilder.getPlaylist("Playlist1");
        Playlist p2 = EntitiesBuilder.getPlaylist("Playlist2");
        p1.add(midiSong);
        p1.add(tsSong);
        p2.add(tsSong);
        database.savePlaylist(p1);
        database.savePlaylist(p2);
        List<String> allPlaylists = database.getAllPlaylists();
        assertEquals("The number of elements are different", allPlaylists.size(), 2);
    }

    /**
     * Create a default midi song
     */
    private MidiSong createMidi(){
        MidiSong midi = EntitiesBuilder.getMidiSong();
        midi.setName("midiTest");
        midi.setPath("C:Test");
        return midi;
    }

    /**
     * Create a default timeslice song
     */
    private TimeSlicesSong createTS(){
        TimeSlicesSong ts = EntitiesBuilder.getTimeSlicesSong();
        ts.setName("tssTest");
        TimeSlice t = new TimeSlice();
        t.setBpm(60);
        t.setDurationInBeats(120);
        ts.add(t);
        return ts;
    }

}
