package a2016.soft.ing.unipd.metronomepro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class ShowPeersActivity extends AppCompatActivity {

    private RecyclerView rVShowPeers;
    private RecyclerView.LayoutManager rVLayoutManager;
    //private ShowPeersAdapter showPeersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_peers);

        rVShowPeers = (RecyclerView)findViewById(R.id.recicle_song_for_playlist);
        rVShowPeers.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVShowPeers.setLayoutManager(rVLayoutManager);

//        selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,songForAdapter,this);//adapter initialized
//        rVSelectSong.setAdapter(selectSongForPlaylistAdapter);

    }
}
