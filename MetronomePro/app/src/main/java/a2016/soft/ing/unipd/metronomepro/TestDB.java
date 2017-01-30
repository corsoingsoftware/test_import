package a2016.soft.ing.unipd.metronomepro;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

public class TestDB extends AppCompatActivity {

    private DataProvider dp;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_db);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dp = DataProviderBuilder.getDefaultDataProvider(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidiSong m1 = EntitiesBuilder.getMidiSong();
                m1.setName("m1");
                m1.setPath("asd");
                boolean z = dp.saveSong(m1);
                MidiSong m2 = EntitiesBuilder.getMidiSong();
                m2.setName("m2");
                m2.setPath("asdasd");
                z = dp.saveSong(m2);
                MidiSong m3 = EntitiesBuilder.getMidiSong();
                m3.setName("m3");
                m3.setPath("asdasdasd");
                z = dp.saveSong(m3);
                TimeSlicesSong tss1 = EntitiesBuilder.getTimeSlicesSong();
                TimeSlicesSong tss2 = EntitiesBuilder.getTimeSlicesSong();
                tss1.setName("ts1");
                tss2.setName("ts2");
                TimeSlice ts = new TimeSlice();
                ts.setBpm(60);
                ts.setDurationInBeats(120);
                tss1.add(ts);
                tss2.add(ts);
                ts = new TimeSlice();
                ts.setBpm(60);
                ts.setDurationInBeats(120);
                tss1.add(ts);
                tss2.add(ts);
                ts = new TimeSlice();
                ts.setBpm(60);
                ts.setDurationInBeats(120);
                tss1.add(ts);
                z = dp.saveSong(tss1);
                z = dp.saveSong(tss2);
                Song x = dp.getSong("ts1");
                if (x != null) Log.d("TS", x.getName());
                MidiSong y = (MidiSong) dp.getSong("m1");
                if (y != null) Log.d("MD", y.getName());
                List<Song> allSongs = dp.getAllSongs();
                for (Song s: allSongs) {
                    System.out.println("canzone: " + s.getName());
                }
                Playlist p1 = EntitiesBuilder.getPlaylist("p1");
                Playlist p2 = EntitiesBuilder.getPlaylist("p2");
                p1.add(tss1);
                p1.add(m1);
                p1.add(m3);
                p1.add(tss2);
                p1.add(m2);
                p2.add(m1);
                p2.add(tss2);
                dp.savePlaylist(p1);
                dp.savePlaylist(p2);
                List<String> playlistsName = dp.getAllPlaylists();
                for(String name : playlistsName) System.out.println(name);
                Playlist prova = dp.getPlaylist(p2.getName());
                if (prova != null) Log.d("playlist: ", prova.getName());
                Iterator t = prova.iterator();
                while(t.hasNext()){
                    Song temp = (Song) t.next();
                    Log.d("Canzone di P1: ", temp.getName());
                }
                /*
                MidiSong ms1= EntitiesBuilder.getMidiSong();
                ms1.setName("m1");
                ms1.setPath("C:asdlol");
                MidiSong ms2= EntitiesBuilder.getMidiSong();
                ms2.setName("ms2");
                ms2.setPath("C:asdasd");
                MidiSong ms3= EntitiesBuilder.getMidiSong();
                ms3.setName("ms3");
                ms3.setPath("C:lollol");

                TimeSlicesSong tss1=EntitiesBuilder.getTimeSlicesSong();
                tss1.setName("ts1");
                TimeSlice ts = new TimeSlice();
                ts.setBpm(60);
                ts.setDurationInBeats(120);
                tss1.add(ts);
                ts= new TimeSlice();
                ts.setBpm(60);
                ts.setDurationInBeats(120);
                tss1.add(ts);
                ts= new TimeSlice();
                ts.setBpm(60);
                ts.setDurationInBeats(120);
                tss1.add(ts);
                dp.saveSong(ms1);
                dp.saveSong(ms2);
                dp.saveSong(ms3);
                //dp.saveSong(tss1);
        *//*p1.add(ms1);
        p1.add(tss2);
        p1.add(ms2);
        p1.add(ms3);
        p1.add(tss1);
        p1.add(tss3);
        dp.savePlaylist(p1);
        List<Playlist> returnedPlaylist = dp.getAllPlaylists();
        for(Playlist playlist: returnedPlaylist) Log.e("Playlist: ",playlist.getName());*//*
                //List<Song> returnedSongs = dp.getAllSongs();
                List<Song> returnedSongs = new ArrayList<Song>();
                returnedSongs = dp.getAllSongs();
                for (Song s : returnedSongs){
                    Log.d("Canzone: ", s.getName());
                }*/
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("TestDB Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
