package a2016.soft.ing.unipd.metronomepro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import a2016.soft.ing.unipd.metronomepro.adapters.ModifyPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

public class ModifyPlaylistActivity extends AppCompatActivity implements OnStartDragListener {

    private RecyclerView rVModifyPlaylist;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ModifyPlaylistAdapter modifyPlaylistAdapter;
    private ItemTouchHelper itemTouchHelper;
    private Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rVModifyPlaylist = (RecyclerView) findViewById(R.id.recycler_view_songs_list);
        rVModifyPlaylist.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVModifyPlaylist.setLayoutManager(rVLayoutManager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song songToEdit = EntitiesBuilder.getSong();
                String title = "Titolo della canzone di prova";
                Bundle bundle = new Bundle();
                bundle.putParcelable(title, (Parcelable) songToEdit);
                Intent intent = new Intent();
                startActivityForResult(intent, 0 ,bundle);

            }
        });


        playlist = EntitiesBuilder.getPlaylist("playlist di prova");
//        Song s = EntitiesBuilder.getSong();
//        TimeSlice ts = new TimeSlice();
//        s.add(ts);
//        playlist.add(s);


        if (savedInstanceState != null && savedInstanceState.containsKey("Adapter")) {
            playlist = savedInstanceState.getParcelable("Adapter");

        } else if (savedInstanceState != null && savedInstanceState.containsKey("Playlist")) {
            playlist = savedInstanceState.getParcelable("Playlist");



        } else {
            playlist.add(EntitiesBuilder.getSong("Canzone 1"));
            playlist.add(EntitiesBuilder.getSong("Canzone 2"));
            playlist.add(EntitiesBuilder.getSong("Canzone 3"));
            playlist.add(EntitiesBuilder.getSong("Canzone 4"));
            playlist.add(EntitiesBuilder.getSong("Canzone 5"));
            playlist.add(EntitiesBuilder.getSong("Canzone 6"));
        }
        modifyPlaylistAdapter = new ModifyPlaylistAdapter((ParcelablePlaylist) playlist, this, this);
        rVModifyPlaylist.setAdapter(modifyPlaylistAdapter);
        DragTouchHelperCallback myItemTouchHelper = new DragTouchHelperCallback(modifyPlaylistAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVModifyPlaylist);


    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Adapter", (Parcelable) playlist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Song s = data.getParcelableArrayExtra("SongEdited");
        playlist.add(s);
    }
}
