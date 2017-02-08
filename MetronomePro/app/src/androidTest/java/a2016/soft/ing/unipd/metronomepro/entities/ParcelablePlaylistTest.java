package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

import a2016.soft.ing.unipd.metronomepro.TestGenerators;

import static org.junit.Assert.*;

/**
 * Created by Federico Favotto on 06/02/2017.
 */
public class ParcelablePlaylistTest {
    @Test
    public void writeToParcel() throws Exception {
        ParcelablePlaylist playlist= new ParcelablePlaylist(0,"test");
        playlist.add(TestGenerators.generateRandomTestSong(10));
        playlist.add(TestGenerators.generateRandomTestMidiSong());
        playlist.add(TestGenerators.generateRandomTestMidiSong());
        playlist.add(TestGenerators.generateRandomTestSong(10));
        playlist.add(TestGenerators.generateRandomTestSong(10));
        playlist.add(TestGenerators.generateRandomTestMidiSong());
        playlist.add(TestGenerators.generateRandomTestSong(10));

        Parcel parcel = Parcel.obtain();
        playlist.writeToParcel(parcel, playlist.describeContents());
        parcel.setDataPosition(0);

        ParcelablePlaylist createdFromParcel = ParcelablePlaylist.CREATOR.createFromParcel(parcel);
        for(int i=0; i<playlist.size();i++){
            Song song=playlist.get(i);
            Song fromParcelSong=createdFromParcel.get(i);
            if(song instanceof TimeSlicesSong) {
                assertArrayEquals(((TimeSlicesSong) song).toArray(), ((TimeSlicesSong) fromParcelSong).toArray());
            } else {
                MidiSong midiSong=(MidiSong)song;
                MidiSong fromParcelMidiSong=(MidiSong)fromParcelSong;
                assertEquals(midiSong.getId(),fromParcelMidiSong.getId());
                assertEquals(midiSong.getPath(),fromParcelMidiSong.getPath());
                assertEquals(midiSong.getName(),fromParcelMidiSong.getName());
            }
        }
        assertEquals(playlist.getName(),createdFromParcel.getName());
        assertEquals(playlist.getId(),createdFromParcel.getId());
    }

}