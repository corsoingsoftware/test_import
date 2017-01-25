package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;

import org.junit.Test;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.SelectSongForPlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

import static org.junit.Assert.*;

/**
 * Created by giuli on 25/12/2017.
 */
public class SelectSongForPlaylistAdapterTest {
    private Context context;
    @Test
    public void getArraySongs() throws Exception {


        SelectSongForPlaylistAdapter adapter = new SelectSongForPlaylistAdapter(context,chargeArray());

        int songs = adapter.getArraySongs().size();
        assertEquals(songs,21);

    }
    public ArrayList<Song> chargeArray(){
        Song s0 = EntitiesBuilder.getSong("song 0");
        Song s1 = EntitiesBuilder.getSong("song 1");
        Song s2 = EntitiesBuilder.getSong("song 2");
        Song s3 = EntitiesBuilder.getSong("song 3");
        Song s4 = EntitiesBuilder.getSong("song 4");
        Song s5 = EntitiesBuilder.getSong("song 5");
        Song s6 = EntitiesBuilder.getSong("song 6");
        Song s7 = EntitiesBuilder.getSong("song 7");
        Song s8 = EntitiesBuilder.getSong("song 8");
        Song s9 = EntitiesBuilder.getSong("song 9");
        Song s10 = EntitiesBuilder.getSong("song 10");
        Song s11 = EntitiesBuilder.getSong("song 11");
        Song s12 = EntitiesBuilder.getSong("song 12");
        Song s13 = EntitiesBuilder.getSong("song 13");
        Song s14 = EntitiesBuilder.getSong("song 14");
        Song s15 = EntitiesBuilder.getSong("song 15");
        Song s16 = EntitiesBuilder.getSong("song 16");
        Song s17 = EntitiesBuilder.getSong("song 17");
        Song s18 = EntitiesBuilder.getSong("song 18");
        Song s19 = EntitiesBuilder.getSong("song 19");
        Song s20 = EntitiesBuilder.getSong("song 20");

        ArrayList<Song> array = new ArrayList<>();

        array.add((Song) s0);
        array.add((Song) s1);
        array.add((Song) s2);
        array.add((Song) s3);
        array.add((Song) s4);
        array.add((Song) s5);
        array.add((Song) s6);
        array.add((Song) s7);
        array.add((Song) s8);
        array.add((Song) s9);
        array.add((Song) s10);
        array.add((Song) s11);
        array.add((Song) s12);
        array.add((Song) s13);
        array.add((Song) s14);
        array.add((Song) s15);
        array.add((Song) s16);
        array.add((Song) s17);
        array.add((Song) s18);
        array.add((Song) s19);
        array.add((Song) s20);

        return array;
    }

    @Test
    public void getSelectedSongs() throws Exception {

    }

    @Test
    public void onCreateViewHolder() throws Exception {

    }

    @Test
    public void onBindViewHolder() throws Exception {

    }

    @Test
    public void onViewRecycled() throws Exception {

    }

}