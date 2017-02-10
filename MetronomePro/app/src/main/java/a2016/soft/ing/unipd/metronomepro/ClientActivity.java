package a2016.soft.ing.unipd.metronomepro;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

import a2016.soft.ing.unipd.metronomepro.adapters.ShowPeersAdapter;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.SQLiteDataProvider;
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
public class ClientActivity extends AppCompatActivity implements ClientActionListener,ShowPeersAdapter.PeerSelected, SongPlayerServiceCaller.SongPlayerServiceCallerCallback {

    private ShowPeersAdapter showPeersAdapter;
    private RecyclerView rVshowPeersList;
    private RecyclerView.LayoutManager rVLayoutManager;
    private DataProvider provider;
    private FloatingActionButton fabToSnack;

    private Manager connectionManager;
    private Client client;
    private SongPlayerServiceCaller spsc;
    private HashMap<String, Song> songsOfPlaylist;
    private Song[] songsToPlay;
    private boolean bluetoothOk = false, serviceOk = false;
    HashMap<String, Song> allSongs;
    long timeDiff;
    private static final int REQUEST_ENABLE_BT = 1;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_peers);

        rVshowPeersList = (RecyclerView) findViewById(R.id.recycler_peer);
        fabToSnack=(FloatingActionButton)findViewById(R.id.fabOk);
        rVshowPeersList.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVshowPeersList.setLayoutManager(rVLayoutManager);
        showPeersAdapter = new ShowPeersAdapter(new ArrayList<Peer>(20), this);
        rVshowPeersList.setAdapter(showPeersAdapter);
        provider = DataProviderBuilder.getDefaultDataProvider(this);
        allSongs = new HashMap<>(200);
        List<Song> songs = provider.getAllSongs();
        for (Song song : songs) {
            allSongs.put(song.getName(), song);
        }
        spsc = new SongPlayerServiceCaller(this, this);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            }
        }
        try {

            connectionManager = ManagerFactory.bluetoothInstance();
            client = connectionManager.newClient(this, this);

        } catch (AdapterNotActivatedException ex) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } catch (Exception ex) {
            //def action
            ex.printStackTrace();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        client.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    try {
                        connectionManager = ManagerFactory.bluetoothInstance();
                        client = connectionManager.newClient(this, this);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onPeerListReady(List<Peer> list) {
        out.println("");
        if (list.size() > 0) {
            showPeersAdapter.refreshPeers(list);
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
        timeDiff = timeDifference;
        bluetoothOk = true;
        if (bluetoothOk && serviceOk) {
            faiqualcosa();
        }
        makeToast("Syncronized to Server!");
    }

    private void makeToast(final String s, final int duration){
        final Context a =this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Snackbar.make(fabToSnack,s,duration).show();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    private void makeToast(final String s){
        makeToast(s,Snackbar.LENGTH_LONG);
    }

    private void faiqualcosa() {
        makeToast("service connected");
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
        long a = System.currentTimeMillis();
        long b = time + (timeDiff);
        while (a < b - 7) {
            a = System.currentTimeMillis();
        }
        spsc.play();
        a = System.currentTimeMillis();
        out.println(a - b);
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


    public void manageGeneralMessage(String s) {
        int a = s.indexOf(":");
        String toManage = s.substring(0, a);
        if (toManage.equals("playlist")) {
            refreshPlaylist(s.substring(a + 1, s.length() - 1));
        } else if (toManage.equals("next")) {
            refreshNext(s.substring(a + 1, s.length() - 1));
        }
    }

    private void refreshPlaylist(String songs) {
        String[] titles = songs.split(";");
        songsOfPlaylist = new HashMap<>(20);
        for (int i = 0; i < titles.length; i++) {
            if (allSongs.containsKey(titles[i])) {
                songsOfPlaylist.put(titles[i], allSongs.get(titles[i]));
                spsc.load(allSongs.get(titles[i]));
            }else{
                makeToast("This song doesn't exist! "+titles[i], Snackbar.LENGTH_LONG);
            }
        }
    }

    private void refreshNext(String next) {
        String[] titles = next.split(";");
        songsToPlay = new Song[titles.length];

        for (int i = 0; i < titles.length; i++) {
            songsToPlay[i] = songsOfPlaylist.get(titles[i]);
        }
    }

    @Override
    public void serviceConnected() {
        serviceOk = true;
        if (bluetoothOk && serviceOk) {
            faiqualcosa();
        }
    }

    @Override
    public void onPeerSelected(Peer peer) {
        client.connectToPeer(peer);
    }
}

