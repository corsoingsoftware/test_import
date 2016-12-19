package a2016.soft.ing.unipd.metronomepro.data.access.layer;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.v4.content.ContextCompat;

import org.junit.Test;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

import static org.junit.Assert.*;

/**
 * Created by Francesc on 19/12/2016.
 */
public class FileDataProvierTest {

    @Test
    public void getSongs() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        Song songToSave = EntitiesBuilder.getSong();
        Song songToSave1 = EntitiesBuilder.getSong();
        Song songToSave2= EntitiesBuilder.getSong();
        Song songToSave3 = EntitiesBuilder.getSong();
        FileDataProvier fdp = new FileDataProvier(context);
        fdp.save(songToSave);
        fdp.save(songToSave1);
        fdp.save(songToSave2);
        fdp.save(songToSave3);
        ArrayList<Song> arrayToCompare = new ArrayList<Song>();
        arrayToCompare.add(songToSave);
        arrayToCompare.add(songToSave1);
        arrayToCompare.add(songToSave2);
        arrayToCompare.add(songToSave3);
        fdp.getSongs();//.equals(arrayToCompare);
    }

    @Test
    public void getSongs1() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        Song songToSave = EntitiesBuilder.getSong();
        FileDataProvier fdp = new FileDataProvier(context);
        fdp.save(songToSave);
        fdp.getSongs(songToSave.getName(), null);//.equals(songToSave);
    }

    @Test
    public void getPlaylists() throws Exception {

    }

    @Test
    public void save() throws Exception {
        Song songToSave = EntitiesBuilder.getSong("Canzone di prova");
        Context context = InstrumentationRegistry.getTargetContext();
        FileDataProvier fdp = new FileDataProvier(context);
        fdp.save(songToSave);
        fdp.getSongs(songToSave.getName(), null);//.equals(songToSave);
    }

    @Test
    public void deleteSong() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        Song songToDelete = EntitiesBuilder.getSong();
        FileDataProvier fdp = new FileDataProvier(context);
        fdp.save(songToDelete);
        fdp.deleteSong(songToDelete);
        fdp.getSongs(songToDelete.getName(), null);
    }

    @Test
    public void savePlaylist() throws Exception {

    }

    @Test
    public void deletePlaylist() throws Exception {

    }

}