package a2016.soft.ing.unipd.metronomepro;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import org.group3.sync.Client;
import org.group3.sync.ClientActionListener;
import org.group3.sync.Manager;
import org.group3.sync.ManagerFactory;
import org.group3.sync.MetroConfig;
import org.group3.sync.Peer;
import org.group3.sync.exception.ErrorCode;

import java.util.List;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerServiceCaller;

import static java.lang.System.out;

/**
 * Questa classe deve permettere all'utente di associarsi al server o cercarne uno.
 * Una volta fatto avvia l'activity di Omar nel caso sia server, altrimenti la clientactivity
 */
public class ConnectionActivity extends AppCompatActivity implements ClientActionListener, SongPlayerServiceCaller.SongPlayerServiceCallerCallback {

    Manager connectionManager;
    Client client;
    SongPlayerServiceCaller spsc;
    boolean bluetoothOk=false, serviceOk=false;
    long timeDiff;
    TimeSlicesSong ts;
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
        connectionManager= ManagerFactory.bluetoothInstance();
        try {

            client=connectionManager.newClient(this,this);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onPeerListReady(List<Peer> list) {
        out.println("");
        if(list.size()>0){
            client.connectToPeer(list.get(0));
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
        while(System.currentTimeMillis()-timeDiff<time){
        }
        spsc.write(new Song[]{ts});
        out.println("");
    }

    @Override
    public void onReceiveStop() {

        out.println("");
    }

    @Override
    public void onReceiveGeneralMessage(byte[] message) {

        out.println("");
    }


    @Override
    public void serviceConnected() {
        ts= EntitiesBuilder.getTimeSlicesSong();
        ts.setName("prova");
        TimeSlice a= new TimeSlice();
        a.setBpm(60);
        a.setDurationInBeats(200);
        ts.add(a);
        spsc.load(ts);
        serviceOk=true;
        if(bluetoothOk&&serviceOk){
            faiqualcosa();
        }
    }
}
