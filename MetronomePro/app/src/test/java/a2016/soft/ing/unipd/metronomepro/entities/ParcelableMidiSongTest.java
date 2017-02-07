package a2016.soft.ing.unipd.metronomepro.entities;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Alberto on 05/02/17.
 */
public class ParcelableMidiSongTest {
    private String pathTest = "Device/Path/Directory";
    private String titleTest = "Test title";
    private ParcelableMidiSong midiSongTest = new ParcelableMidiSong(titleTest,pathTest);
    @Test
    public void getPath() throws Exception {
        assertNotNull(midiSongTest.getPath());
        assertEquals(midiSongTest.getPath(),pathTest);

    }

    @Test
    public void setPath() throws Exception {
        String newPath = "Device/Path/Dir/newDir";
        midiSongTest.setPath(newPath);
        assertEquals(midiSongTest.getPath(),newPath);

    }

    @Test
    public void getName() throws Exception {
        assertNotNull(midiSongTest.getName());
        assertEquals(midiSongTest.getName(),titleTest);

    }

    @Test
    public void setName() throws Exception {
        String newTitle = "Title 2";
        midiSongTest.setName(newTitle);
        assertEquals(midiSongTest.getName(),newTitle);

    }

    @Test
    public void getSongPlayer() throws Exception {

    }

}