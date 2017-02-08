package a2016.soft.ing.unipd.metronomepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.group3.sync.Manager;
import org.group3.sync.ManagerFactory;
import org.group3.sync.Peer;
import org.group3.sync.Server;
import org.group3.sync.ServerActionListener;
import org.group3.sync.ServerInfo;
import org.group3.sync.exception.ErrorCode;

import static java.lang.System.out;

/**
 * Questa classe deve permettere all'utente di associarsi al server o cercarne uno.
 * Una volta fatto avvia l'activity di Omar nel caso sia server, altrimenti la clientactivity
 */
public class ConnectionActivity extends AppCompatActivity implements ServerActionListener {

    Manager connectionManager;
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
            Server server=connectionManager.newServer(this, this);

        }
        catch (Exception ex){

        }

    }

    @Override
    public void onClientConnected(Peer peer) {
        out.println(getClass().getEnclosingMethod().getName());
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
}
