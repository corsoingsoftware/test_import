package a2016.soft.ing.unipd.metronomepro;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.group3.sync.Client;
import org.group3.sync.ClientActionListener;
import org.group3.sync.Manager;
import org.group3.sync.ManagerFactory;
import org.group3.sync.MetroConfig;
import org.group3.sync.Peer;
import org.group3.sync.exception.AdapterNotActivatedException;
import org.group3.sync.exception.ErrorCode;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerServiceCaller;

import static java.lang.System.out;

/**
 * Activity che riceve i comandi del server e li esegue
 */
public class ClientActivity extends AppCompatActivity implements ClientActionListener, SongPlayerServiceCaller.SongPlayerServiceCallerCallback {

    Manager connectionManager;
    Client client;
    SongPlayerServiceCaller spsc;
    HashMap<String,Song> songsOfPlaylist;
    Song[] songsToPlay;
    boolean bluetoothOk=false, serviceOk=false;
    long timeDiff;
    private static final int REQUEST_ENABLE_BT=1;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_activity);

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
            client=connectionManager.newClient(this,this);

        }catch (AdapterNotActivatedException ex){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }catch (Exception ex){
            //def action
            ex.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK){
                    try {
                        client = connectionManager.newClient(this, this);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onPeerListReady(List<Peer> list) {
        out.println("");
        if(list.size()>0){
            for(Peer p : list){
                if(p.getAddress().equals("2C:8A:72:65:DF:4D")) {
                    client.connectToPeer(p);
                    break;
                }
            }
        }

    }

    @Override
    public void onConnecting() {

        out.println("");
    }

    @Override
    public void onConnected() {

        out.println("");
    }

    @Override
    public void onSynchronized(long timeDifference) {
        timeDiff=timeDifference;
        bluetoothOk=true;
        if(bluetoothOk&&serviceOk){
            faiqualcosa();
        }
        out.println("");
    }

    private void faiqualcosa(){

    }

    @Override
    public void onDisconnected() {

        out.println("");
    }

    @Override
    public void onError(ErrorCode error) {

        out.println("");
    }

    @Override
    public void onReceiveSet(MetroConfig config) {

        out.println("");
    }

    @Override
    public void onReceiveStart(MetroConfig config, long time, long delay) {

        spsc.write(songsToPlay);
        long a=System.currentTimeMillis();
        long b=time+(timeDiff);
        while(a<b-10){
            a=System.currentTimeMillis();
        }
        spsc.play();
        a=System.currentTimeMillis();
        out.println(a-b);
        out.println("");
    }

    @Override
    public void onReceiveStop() {

        out.println("");
    }

    @Override
    public void onReceiveGeneralMessage(byte[] message) {
        String string = new String(message, Charset.forName("UTF-8"));
        manageGeneralMessage(string);
        out.println(string);
    }


    public void manageGeneralMessage(String s){
        int a=s.indexOf(":");
        String toManage=s.substring(0,a);
        if (toManage.equals("playlist")) {
            refreshPlaylist(s.substring(a+1,s.length()-1));
        } else if (toManage.equals("next")) {
            refreshNext(s.substring(a+1,s.length()-1));
        }
    }

    private void refreshPlaylist(String songs){
        songsOfPlaylist= new HashMap<>();

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
        songsOfPlaylist.put(midiS1.getName(),midiS1);
        songsOfPlaylist.put(midiS2.getName(),midiS2);
        songsOfPlaylist.put(s1.getName(),s1);
        songsOfPlaylist.put(s2.getName(),s2);
        songsOfPlaylist.put(s3.getName(),s3);
        for(Song s : songsOfPlaylist.values()){
            spsc.load(s);
        }
    }

    private void refreshNext(String next){
        String[] titles=next.split(";");
        songsToPlay= new Song[titles.length];

        for(int i=0; i<titles.length;i++){
            songsToPlay[i]=songsOfPlaylist.get(titles[i]);
        }
    }

    @Override
    public void serviceConnected() {
//        ts= EntitiesBuilder.getTimeSlicesSong();
//
//        ts.setName("prova");
//        TimeSlice a= new TimeSlice();
//        a.setBpm(60);
//        a.setDurationInBeats(3);
//        ts.add(a);
//        a= new TimeSlice();
//        a.setBpm(120);
//        a.setDurationInBeats(4);
//        ts.add(a);
//        a= new TimeSlice();
//        a.setBpm(300);
//        a.setDurationInBeats(4);
//        ts.add(a);
//        spsc.load(ts);
        serviceOk=true;
        if(bluetoothOk&&serviceOk){
            faiqualcosa();
        }
    }
}

