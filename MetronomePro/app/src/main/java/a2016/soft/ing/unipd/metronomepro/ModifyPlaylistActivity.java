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

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.adapters.ModifyPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.DragTouchHelperCallback;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.PlayableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.sound.management.SongPlayerServiceCaller;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.*;

public class ModifyPlaylistActivity extends AppCompatActivity implements OnStartDragListener {

    private static final String PLAYLIST_DEFAULT_NAME="def-playlist";

    private RecyclerView rVModifyPlaylist;
    private RecyclerView.LayoutManager rVLayoutManager;
    private ModifyPlaylistAdapter modifyPlaylistAdapter;
    private ItemTouchHelper itemTouchHelper;
    private Playlist playlist;
    private DataProvider database;
    SongPlayerServiceCaller spsc;
    private ArrayList<Song> songsToAdd = new ArrayList<>();//creata da giulio: sono le canzoni che vengono
                                                                    //selezionte nel layout di giulio
    //All results:
    private static final int START_EDIT_NEW_SONG=1;

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
        database = DataProviderBuilder.getDefaultDataProvider(this);
        final Activity activity=this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            //creato da giulio: mi passi una playlist sottoforma di array di canzoni
            public void onClick(View view) {
              //  Playlist playlistToEdit = modifyPlaylistAdapter.getPlaylistToModify();
                Intent intent = new Intent(activity,SelectSongForPlaylist.class);
                intent.putParcelableArrayListExtra(PLAYLIST,modifyPlaylistAdapter.getAllSongs());
                startActivityForResult(intent, START_EDIT_NEW_SONG);
            }
            /**public void onClick(View view) {
                Song songToEdit = EntitiesBuilder.getSong();
                Intent intent = new Intent(activity,SelectSongForPlaylist.class);
                intent.putExtra(SONG_TO_EDIT, (Parcelable) songToEdit);
                startActivityForResult(intent, START_EDIT_NEW_SONG);

            }*/
        });

        FloatingActionButton floatingActionButtonPlay= (FloatingActionButton)findViewById(R.id.fabPlay);
        floatingActionButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.savePlaylist(playlist);
                Intent intent = new Intent(activity, SelectNextSongs.class);
                intent.putExtra(PLAYLIST, (Parcelable) modifyPlaylistAdapter.getPlaylistToModify());
                startActivity(intent);
            }
        });

        playlist = EntitiesBuilder.getPlaylist(PLAYLIST_SELECTED);
        if (savedInstanceState != null && savedInstanceState.containsKey(PLAYLIST)) {
            //saved state on destroy
            playlist = savedInstanceState.getParcelable(PLAYLIST);

        }/* Federico: ho tolto un if in più che attualmente non serve
        else if (savedInstanceState != null && savedInstanceState.containsKey("Playlist")) {
            playlist = savedInstanceState.getParcelable("Playlist");
        } */
        else {
            //Default
            try {
                //DataProvider dp = DataProviderBuilder.getDefaultDataProvider(this);
                //TODO Modified by Mune, I touched the 2 methods below, if they're wrong just fix them (sorry <3)
           //     List<Song> songs = dp.getAllSongs();
             //   playlist.addAll(songs);
             //   database.savePlaylist(playlist);
            }catch (Exception ex){
                ex.printStackTrace();
//                playlist.add(EntitiesBuilder.getSong("canzone 1"));
//                playlist.add(EntitiesBuilder.getSong("canzone 2"));
//                playlist.add(EntitiesBuilder.getSong("canzone 3"));
//                playlist.add(EntitiesBuilder.getSong("canzone 4"));
      //          playlist.add(EntitiesBuilder.getMidiSong());
            }
            /**
             * creato da giulio: riceve le canzoni che ho selezionato
             */
            Intent intent = getIntent();
            //se mi viene passata una playlist da modificare
            if(intent!=null&&intent.hasExtra(PLAYLIST_SELECTED)){
                try {
                    playlist = intent.getParcelableExtra(PLAYLIST_SELECTED);//canzoni passate dalla playlist (PlaylistView)
                    onSaveInstanceState(intent.getBundleExtra(PLAYLIST_SELECTED));
                    database.savePlaylist(playlist);
   //               playlist = database.getPlaylist(playlist.getName());
  //                playlist = database.getPlaylist(PLAYLIST_SELECTED);

                    //           songsToAdd = intent.<Song>getParcelableArrayListExtra(SONG_TO_ADD); //canzoni passate dal SelectSongForPlaylist
        //            playlist.addAll(songsToAdd);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            //se mi vengono passate canzoni da aggiungere
            else if(intent!=null&&intent.hasExtra(SONG_TO_ADD)){
                try {
                    songsToAdd = intent.<Song>getParcelableArrayListExtra(SONG_TO_ADD); //canzoni passate dal SelectSongForPlaylist
                    playlist.addAll(songsToAdd);
                    database.savePlaylist(playlist);
                    onSaveInstanceState(intent.getBundleExtra(SONG_TO_ADD));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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
        outState.putParcelable(PLAYLIST, (Parcelable) playlist);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case START_EDIT_NEW_SONG:
                    //modifyPlaylistAdapter.addSong((ParcelableSong)data.getParcelableExtra(SONG_TO_EDIT));
                    modifyPlaylistAdapter.addAllSongs(data.<Song>getParcelableArrayListExtra(SONG_TO_ADD));
                    break;
            }
        }
    }

    public void serviceConnected(Song song) {
        final Activity activity=this;
        Intent intent = new Intent(activity, SelectNextSongs.class);
        intent.putExtra(PLAYLIST, (Parcelable) song);
        startActivity(intent);
    }
}
