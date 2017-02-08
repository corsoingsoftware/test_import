package a2016.soft.ing.unipd.metronomepro.entities;

import android.os.Parcel;

import org.junit.Test;

import java.sql.Time;
import java.util.Random;

import static a2016.soft.ing.unipd.metronomepro.TestGenerators.generateRandomTestSong;
import static org.junit.Assert.*;

/**
 * Created by Federico Favotto on 06/02/2017.
 */
public class ParcelableTimeSlicesSongTest {


    @Test
    public void encode() throws Exception {
        encodeThenDecode();
    }


    private void encodeThenDecode(){
        //test empty song
        ParcelableTimeSlicesSong songToTest=generateRandomTestSong(0);
        byte[] songEncoded=songToTest.encode();
        ParcelableTimeSlicesSong songDecoded= new ParcelableTimeSlicesSong(songToTest.getName(),songToTest.getId());
        songDecoded.decode(songEncoded);
        assertArrayEquals(songToTest.toArray(),songDecoded.toArray());

        //test normal song
        songToTest=generateRandomTestSong(10);
        songEncoded=songToTest.encode();
        songDecoded= new ParcelableTimeSlicesSong(songToTest.getName(),songToTest.getId());
        songDecoded.decode(songEncoded);
        assertArrayEquals(songToTest.toArray(),songDecoded.toArray());

        //test overflow song
        songToTest=generateRandomTestSong(6000);
        songEncoded=songToTest.encode();
        songDecoded= new ParcelableTimeSlicesSong(songToTest.getName(),songToTest.getId());
        songDecoded.decode(songEncoded);
        assertArrayEquals(songToTest.toArray(),songDecoded.toArray());
    }

    @Test
    public void decode() throws Exception {
        encodeThenDecode();
    }

    /**
     * Test the parcelable in song!
     * @throws Exception
     */
    @Test
    public void writeToParcel() throws Exception {
        ParcelableTimeSlicesSong songToTest=generateRandomTestSong(50);
        Parcel parcel = Parcel.obtain();
        songToTest.writeToParcel(parcel, songToTest.describeContents());
        parcel.setDataPosition(0);

        ParcelableTimeSlicesSong createdFromParcel = ParcelableTimeSlicesSong.CREATOR.createFromParcel(parcel);
        assertArrayEquals(songToTest.toArray(),createdFromParcel.toArray());
        assertEquals(songToTest.getName(),createdFromParcel.getName());
        assertEquals(songToTest.getId(),createdFromParcel.getId());
    }

}