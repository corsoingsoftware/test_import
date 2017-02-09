package a2016.soft.ing.unipd.metronomepro;

import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.ActivityExtraNames;
/**
 * Created by giuli on 27/12/2016.
 */

/**
 * this class allows to add, create, select and delate a playlists into a list of playlists
 * this is the managment of multiple playlisists
 */
public class PlaylistView extends AppCompatActivity implements SelectPlaylistAdapter.OnPlaylistClickListener, OnStartDragListener {

    private RecyclerView rVPlaylistItem;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectPlaylistAdapter playListAdapter;
    private ArrayList<Playlist> selectedPlaylist;//playlist the user select
    private ItemTouchHelper itemTouchHelper;
    private DataProvider db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rVPlaylistItem = (RecyclerView)findViewById(R.id.recicle_view_playlist);
        rVPlaylistItem.setHasFixedSize(true);
        rVLayoutManager =  new LinearLayoutManager(this);
        rVPlaylistItem.setLayoutManager(rVLayoutManager);

        db = DataProviderBuilder.getDefaultDataProvider(this);
        /**
         * the method getAllPlaylist() of the database returns a list of strings
         * the list of strings are the list of the names of the playlists into the db
         * it doesn't return playlist! just string
         */
        List<String> playlistNames = db.getAllPlaylists();//take the names of the playlists
        selectedPlaylist= new ArrayList<>();
        for (String s:playlistNames){
            Playlist playlistByName = db.getPlaylist(s);//search the names into the database
            selectedPlaylist.add(playlistByName);//and get the playlists
        }

        playListAdapter= new SelectPlaylistAdapter(this,selectedPlaylist,this,this);
        rVPlaylistItem.setAdapter(playListAdapter);
        DragTouchHelperCallback myItemTouchHelper = new DragTouchHelperCallback(playListAdapter);
        itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rVPlaylistItem);
        //restoring the instances
        if(savedInstanceState !=null && savedInstanceState.containsKey(ActivityExtraNames.PLAYLIST_FOR_SELECT)){
            selectedPlaylist = savedInstanceState.getParcelableArrayList(ActivityExtraNames.PLAYLIST_FOR_SELECT);
            playListAdapter = new SelectPlaylistAdapter(this,selectedPlaylist,this,this);
            rVPlaylistItem.setAdapter(playListAdapter);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog();
            }
        });
    }
    protected void onSaveInstanceState(Bundle outState) {
        //it takes the whole playlists
        outState.putParcelableArrayList(ActivityExtraNames.PLAYABLE_PLAYLIST, (ArrayList<? extends Parcelable>) playListAdapter.getArrayPlaylist());

        super.onSaveInstanceState(outState);
    }

    /**
     * to create a new playlist
     */
    public void customDialog(){
        final Dialog dialog = new Dialog(PlaylistView.this);
        dialog.setContentView(R.layout.dialog_submit);
        final EditText edit_name = (EditText) dialog.findViewById(R.id.edit_name_p);
        Button cancel = (Button) dialog.findViewById(R.id.but_cancel_p);
        Button submit = (Button) dialog.findViewById(R.id.but_submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Playlist playlistToEdit = EntitiesBuilder.getPlaylist(edit_name.getText().toString());
                playListAdapter.addPlaylist(playlistToEdit);
                dialog.cancel();
            }
        });
        dialog.show();

    }

    /**
     * go to modify playlist activity
     */
    @Override
    public void onPlaylistClick() {
        Activity activity = this;
        Intent intent = new Intent(activity,ModifyPlaylistActivity.class);
        Playlist playlistToEdit = playListAdapter.getPlaylistToEdit();
        intent.putExtra(ActivityExtraNames.PLAYLIST_SELECTED,playListAdapter.getPlaylistToEdit());
        //Playlist playList = playListAdapter.getPlaylistToEdit();
        startActivity(intent);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
    }
}
