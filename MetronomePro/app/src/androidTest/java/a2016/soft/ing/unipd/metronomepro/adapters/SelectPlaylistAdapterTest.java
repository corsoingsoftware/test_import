package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;

import org.junit.Test;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

import static org.junit.Assert.*;

/**
 * Created by giuli on 12/01/2017.
 */
public class SelectPlaylistAdapterTest {

    private Context context;
    private SelectPlaylistAdapter.OnPlaylistClickListener playlistClickListener;

    public ArrayList<Playlist> chargeAdapter(){
        //three different Playlist within Different Songs
        Playlist p1 =  EntitiesBuilder.getPlaylist("prova di playlist 1");
        Playlist p2 =  EntitiesBuilder.getPlaylist("prova di playlist 2");
        Playlist p3 =  EntitiesBuilder.getPlaylist("prova di playlist 3");

        p1.add(EntitiesBuilder.getSong("song 1"));

        p2.add(EntitiesBuilder.getSong("song 1"));
        p2.add(EntitiesBuilder.getSong("song 2"));

        p3.add(EntitiesBuilder.getSong("song 1"));
        p3.add(EntitiesBuilder.getSong("song 2"));
        p3.add(EntitiesBuilder.getSong("song 3"));



        ArrayList<Playlist> arrayList = new ArrayList<>();

        arrayList.add(0,p1);
        arrayList.add(1,p2);
        arrayList.add(2,p3);

        return arrayList;
    }
    @Test
    public void addPlaylist() throws Exception {
        Playlist p4 =  EntitiesBuilder.getPlaylist("prova di playlist 3");

        p4.add(EntitiesBuilder.getSong("song 1"));
        p4.add(EntitiesBuilder.getSong("song 2"));
        p4.add(EntitiesBuilder.getSong("song 3"));
        p4.add(EntitiesBuilder.getSong("song 4"));

        SelectPlaylistAdapter adapter = new SelectPlaylistAdapter(context,chargeAdapter(),playlistClickListener);
        adapter.addPlaylist(p4);
        int size = adapter.getItemCount();
        //se addPlaylist funziona dentro alla lista ci sono 4 canzoni
        assertEquals(size,4);

    }

    @Test
    public void remuvePlaylist() throws Exception {
        ArrayList<Playlist> list = chargeAdapter();
        SelectPlaylistAdapter adapter = new SelectPlaylistAdapter(context,list,playlistClickListener);
        adapter.remuvePlaylist(0);

        list.remove(0);

        assertSame(adapter.getArrayPlaylist(),list);
    }

    @Test
    public void getArrayPlaylist() throws Exception {
        SelectPlaylistAdapter adapter = new SelectPlaylistAdapter(context,chargeAdapter(),playlistClickListener);
        int songs = adapter.getArrayPlaylist().size();
        assertEquals(songs,3);
    }
}