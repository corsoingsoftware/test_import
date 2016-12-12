package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import a2016.soft.ing.unipd.metronomepro.adapters.ModifyPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.TimeSlicesAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

public class ModifyPlaylistActivity extends AppCompatActivity implements OnStartDragListener {

    private RecyclerView rVModifyPlaylist;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ModifyPlaylistAdapter ModifyPalylistAdapter;
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
        ModifyPalylistAdapter = new ModifyPlaylistAdapter(playlist, this);
        rVModifyPlaylist.setAdapter(ModifyPalylistAdapter);
        DragTouchHelperCallback myItemTouchHelper = new DragTouchHelperCallback(ModifyPalylistAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVModifyPlaylist);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
        @Override
        public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
            itemTouchHelper.startDrag(viewHolder);
        }
    }
