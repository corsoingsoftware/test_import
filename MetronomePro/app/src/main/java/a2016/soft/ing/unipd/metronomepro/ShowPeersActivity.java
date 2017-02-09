package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.group3.sync.Peer;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.ShowPeersAdapter;

public class ShowPeersActivity extends AppCompatActivity {
    private ShowPeersAdapter showPeersAdapter;
    private RecyclerView rVshowPeersList;
    private RecyclerView.LayoutManager rVLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_peers);
        //   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //     setSupportActionBar(toolbar);
        rVshowPeersList = (RecyclerView) findViewById(R.id.recycler_peer);
        rVshowPeersList.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVshowPeersList.setLayoutManager(rVLayoutManager);
        showPeersAdapter = new ShowPeersAdapter(test(),null);
        rVshowPeersList.setAdapter(showPeersAdapter);
        Peer ptoAdd = new Peer("aggiunta","aggiunta","aggiunta");
        showPeersAdapter.addPeer(ptoAdd);
        showPeersAdapter.removePeer(2);
        showPeersAdapter.removePeer(ptoAdd);

    }

    public ArrayList<Peer> test() {
        ArrayList<Peer> peerList = new ArrayList<Peer>();
        Peer p1 = new Peer("nome1", "via garibaldi", "un telefono");
        Peer p2 = new Peer("nome2", "via poppe", "un altro telefono");
        Peer p3 = new Peer("nome3", "via franchetti", "un terzo telefono");
        peerList.add(p1);
        peerList.add(p3);
        peerList.add(p2);
        return peerList;
    }
}

