package a2016.soft.ing.unipd.metronomepro;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.group3.sync.Manager;
import org.group3.sync.ManagerFactory;
import org.group3.sync.Peer;
import org.group3.sync.Server;
import org.group3.sync.ServerActionListener;
import org.group3.sync.ServerInfo;
import org.group3.sync.exception.ErrorCode;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.MidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayer;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerServiceCaller;

import static java.lang.System.out;

/**
 * Questa classe deve permettere all'utente di associarsi al server o cercarne uno.
 * Una volta fatto avvia l'activity di Omar nel caso sia server, altrimenti la clientactivity
 */
public class ConnectionActivity extends AppCompatActivity implements ServerActionListener, SongPlayerServiceCaller.SongPlayerServiceCallerCallback {

    Manager connectionManager;
    SongPlayerServiceCaller spsc;
    long timeDifference;
    TimeSlicesSong ts;
    MidiSong midiS2;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_connection_activity);
        connectionManager= ManagerFactory.bluetoothInstance();//grande Lì!
        try {
            final Server server=connectionManager.newServer(this, this);
            spsc = new SongPlayerServiceCaller(this, this);
        }
        catch (Exception ex){

        }

    }

    @Override
    public void onClientConnected(Peer peer) {
        out.println("onClientConnected");
    }

    @Override
    public void onClientSynchronized(Peer peer) {
        out.println("onClientSynchronized");
    }

    @Override
    public void onClientDisconnected(Peer peer) {
        out.println("onClientDisconnected");
    }

    @Override
    public void onServerStarted(ServerInfo serverInfo) {
        out.println("onServerStarted");
    }

    @Override
    public void onServerStopped() {
        out.println("onServerStopped");
    }

    @Override
    public void onError(ErrorCode error) {
        out.println("onError");
    }

    @Override
    public void serviceConnected() {
        /*ts= EntitiesBuilder.getTimeSlicesSong();
        ts.setName("prova");
        TimeSlice a= new TimeSlice();
        a.setBpm(60);
        a.setDurationInBeats(3);
        ts.add(a);
        a= new TimeSlice();
        a.setBpm(120);
        a.setDurationInBeats(4);
        ts.add(a);
        a= new TimeSlice();
        a.setBpm(300);
        a.setDurationInBeats(4);
        ts.add(a);
        spsc.load(ts);*/
        midiS2 = (MidiSong)EntitiesBuilder.getMidiSong();
        midiS2.setPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                + "/A.mid");
        midiS2.setName("midiSong2");
        spsc.load(midiS2);

    }
}
