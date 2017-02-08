package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.group3.sync.Peer;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.ShowPeersAdapter;

public class ShowPeersActivity extends AppCompatActivity {

    private RecyclerView rVShowPeers;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ShowPeersAdapter showPeersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_peers);

        rVShowPeers = (RecyclerView) findViewById(R.id.recycler_peer);
//        rVShowPeers.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVShowPeers.setLayoutManager(rVLayoutManager);
        ArrayList<Peer> peerList = new ArrayList<Peer>();
        test(peerList);
        showPeersAdapter = new ShowPeersAdapter(peerList);
        rVShowPeers.setAdapter(showPeersAdapter);
    }

    public void test(ArrayList<Peer> peerList) {
        Peer p1 = new Peer("nome1", "via garibaldi", "un telefono");
        Peer p2 = new Peer("nome2", "via poppe", "un altro telefono");
        Peer p3 = new Peer("nome3", "via franchetti", "un terzo telefono");
        peerList.add(p1);
        peerList.add(p3);
        peerList.add(p2);
    }
}

