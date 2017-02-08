package a2016.soft.ing.unipd.metronomepro;

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
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_activity);
        connectionManager= ManagerFactory.bluetoothInstance();//grande Lì!
        try {
            final Server server=connectionManager.newServer(this, this);
            spsc = new SongPlayerServiceCaller(this, this);
            Button b = (Button)findViewById(R.id.button2);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final long start = System.currentTimeMillis()+2000;
                    server.broadcastStart(start);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(System.currentTimeMillis()<start);
                            spsc.write(new Song[]{ts});
                        }
                    }).start();
                }
            });
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
        ts= EntitiesBuilder.getTimeSlicesSong();
        ts.setName("prova");
        TimeSlice a= new TimeSlice();
        a.setBpm(60);
        a.setDurationInBeats(200);
        ts.add(a);
        spsc.load(ts);

    }
}
