package a2016.soft.ing.unipd.metronomepro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectSongsAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.PlayableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.sound.management.AudioTrackSongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerServiceCaller;

import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.PLAYABLE_PLAYLIST;
import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.PLAYLIST;

public class SelectNextSongs extends AppCompatActivity implements SongPlayerServiceCaller.SongPlayerServiceCallerCallback {

    private final static int MAX_SELECTABLE = 3;
    SongPlayerServiceCaller spsc;
    Playlist p;
    private RecyclerView rVNextSongs;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectSongsAdapter selectSongsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_next_songs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rVNextSongs = (RecyclerView) findViewById(R.id.recycler_view_next_songs);
        rVNextSongs.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVNextSongs.setLayoutManager(rVLayoutManager);
        spsc = new SongPlayerServiceCaller(this,this);





        if(savedInstanceState!=null) {
            if (savedInstanceState.containsKey(PLAYABLE_PLAYLIST)) {

                //Devo ricostruire il list adapter in modo che sia uguale a prima

                ArrayList<PlayableSong> savedArray = savedInstanceState.getParcelableArrayList(PLAYABLE_PLAYLIST);
                int selectedSongs = savedInstanceState.getInt(PLAYABLE_PLAYLIST);
                selectSongsAdapter = new SelectSongsAdapter(savedArray, selectedSongs, MAX_SELECTABLE);

            } /*else if (savedInstanceState.containsKey(PLAYLIST)) {

                p = savedInstanceState.getParcelable(PLAYLIST);
                selectSongsAdapter = new SelectSongsAdapter(this, p, 0, MAX_SELECTABLE);

            }*/
        }else{
            Intent intent=getIntent();
            if(intent!=null){
                if(intent.hasExtra(PLAYLIST)) {
                    p = intent.getParcelableExtra(PLAYLIST);
                }else{
                    p= EntitiesBuilder.getPlaylist("prova");

                    /*Song testSong1 = SongCreator.createTestSong("song1");
                    Song testSong2 = SongCreator.createTestSong("song2");
                    Song testSong3 = SongCreator.createTestSong("song3");
                    p.add(testSong1);
                    p.add(testSong2);
                    p.add(testSong3);*/

                    TimeSlice t1, t2;
                    t1 = new TimeSlice();
                    t1.setDurationInBeats(10);
                    t1.setBpm(80);
                    t2 = new TimeSlice();
                    t2.setDurationInBeats(10);
                    t2.setBpm(180);
                    TimeSlicesSong s1 = (TimeSlicesSong)EntitiesBuilder.getTimeSlicesSong();
                    s1.add(t1);
                    s1.add(t2);
                    TimeSlicesSong s2 = (TimeSlicesSong)EntitiesBuilder.getTimeSlicesSong();
                    TimeSlice t3, t4;
                    t3 = new TimeSlice();
                    t4 = new TimeSlice();
                    t3.setDurationInBeats(10);
                    t3.setBpm(250);
                    s2.add(t3);
                    TimeSlicesSong s3 = (TimeSlicesSong)EntitiesBuilder.getTimeSlicesSong();
                    t4.setDurationInBeats(10);
                    t4.setBpm(300);
                    s3.add(t4);
                    s1.setName("song1");
                    s2.setName("song2");
                    s3.setName("song3");
                    MidiSong midiS1 = (MidiSong)EntitiesBuilder.getMidiSong();
                    midiS1.setPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                            +"/A.mid");//+ "/Tick.mid");
                    midiS1.setName("midiSong1");
                    MidiSong midiS2 = (MidiSong)EntitiesBuilder.getMidiSong();
                    midiS2.setPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                            + "/Tick.mid");
                    midiS2.setName("midiSong2");
                    p.add(midiS1);
                    p.add(midiS2);
                    p.add(s1);
                    p.add(s2);
                    p.add(s3);

                }
                selectSongsAdapter = new SelectSongsAdapter(this, p, 0, MAX_SELECTABLE);
            }
        }


        rVNextSongs.setAdapter(selectSongsAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(spsc.getService() != null) {

                    //do whatever you want to do after successful binding

                    Song[] songs = selectSongsAdapter.getSongs();

                    /*for (Song entrySong : songs) {

                        spsc.write(entrySong);
                        spsc.play(entrySong);
                    }*/

                    spsc.write(songs);
                    spsc.play();
                }

                //Blocco tutto

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(PLAYABLE_PLAYLIST, (ArrayList<PlayableSong>) selectSongsAdapter.getArraySongs());
        outState.putInt(PLAYABLE_PLAYLIST, selectSongsAdapter.getSelectedSongs());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void serviceConnected() {

        ArrayList<PlayableSong> playlist = selectSongsAdapter.getArraySongs();

        for (int i = 0; i < playlist.size(); i++) {

            spsc.load((Song) playlist.get(i).getInnerSong());
        }
    }

}
