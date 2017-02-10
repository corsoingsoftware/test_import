package a2016.soft.ing.unipd.metronomepro;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.group3.sync.Manager;
import org.group3.sync.ManagerFactory;
import org.group3.sync.Peer;
import org.group3.sync.Server;
import org.group3.sync.ServerActionListener;
import org.group3.sync.ServerInfo;
import org.group3.sync.exception.AdapterNotActivatedException;
import org.group3.sync.exception.ErrorCode;

import java.nio.charset.Charset;
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
import static java.lang.System.out;

public class SelectNextSongs extends AppCompatActivity implements ServerActionListener, SongPlayerServiceCaller.SongPlayerServiceCallerCallback {

    Manager connectionManager;
    Server server;
    private final static int MAX_SELECTABLE = 3;
    private static final int REQUEST_ENABLE_BT=1;
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
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            }
        }
        try {

            connectionManager= ManagerFactory.bluetoothInstance();
            server=connectionManager.newServer(this,this);

        }catch (AdapterNotActivatedException ex){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }catch (Exception ex){
            //def action
            ex.printStackTrace();
        }





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

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
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
                    if (songs != null && songs.length > 0) {
                        spsc.write(songs);
                        final long start = System.currentTimeMillis() + 2000;
                        sendNextSongs(songs);
                        try {
                            Thread.sleep(200);
                        } catch (Exception ex) {

                        }
                        server.broadcastStart(start);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (System.currentTimeMillis() < start) ;
                                spsc.play();
                            }
                        }).start();
                    } else {
                        Snackbar.make(fab,"No Songs Selected",Snackbar.LENGTH_SHORT).show();
                    }
                }

                //Blocco tutto

            }
        });


        TextView tv= (TextView)findViewById(R.id.name_playlist);
        tv.setText(p.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK){
                    try {
                        server = connectionManager.newServer(this, this);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(server!=null){
            try {
                server.terminate();
            }catch(Exception ex){

            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(server!=null){
            try {
                server.terminate();
            }catch(Exception ex){

            }
        }
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

    private void sendPlaylist()
    {
        ArrayList<PlayableSong> songs=selectSongsAdapter.getArraySongs();
        StringBuilder sb= new StringBuilder();
        sb.append("playlist:");
        for(PlayableSong song : songs) {
            sb.append(song.getInnerSong().getName()+";");
        }
        server.broadcastGeneralMessage(sb.toString().getBytes(Charset.forName("UTF-8")));
    }

    private void sendNextSongs(Song[] songs)
    {
        StringBuilder sb= new StringBuilder();
        sb.append("next:");
        for(Song song : songs) {
            sb.append(song.getName()+";");
        }
        server.broadcastGeneralMessage(sb.toString().getBytes(Charset.forName("UTF-8")));
    }


    @Override
    public void onClientConnected(Peer peer) {
       // Toast.makeText(this,"Client conn: "+peer.getName(),Toast.LENGTH_SHORT);
    }

    @Override
    public void onClientSynchronized(Peer peer) {
        //Toast.makeText(this,"Client sync: "+peer.getName(),Toast.LENGTH_SHORT);
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendPlaylist();
            }
        }).start();
    }

    @Override
    public void onClientDisconnected(Peer peer) {
        //Toast.makeText(this,"Client disc: "+peer.getName(),Toast.LENGTH_SHORT);
    }

    @Override
    public void onServerStarted(ServerInfo serverInfo) {
        //Toast.makeText(this,"server started",Toast.LENGTH_SHORT);
    }

    @Override
    public void onServerStopped() {
        //Toast.makeText(this,"ServerStopped",Toast.LENGTH_SHORT);
    }

    @Override
    public void onError(ErrorCode error) {
        //Toast.makeText(this,"error "+error,Toast.LENGTH_SHORT);
    }

}
