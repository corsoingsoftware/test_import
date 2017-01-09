package a2016.soft.ing.unipd.metronomepro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

public class SelectSongForPlaylist extends AppCompatActivity {

    private RecyclerView rVSelectSong;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectSongForPlaylistAdapter selectSongForPlaylistAdapter;
    private DataProvider dataProvider;
    private ArrayList<ParcelableSong> playlistSongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song_for_playlist2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rVSelectSong = (RecyclerView)findViewById(R.id.recicle_song_for_playlist);
        rVSelectSong.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVSelectSong.setLayoutManager(rVLayoutManager);
        selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,provaDiTest());
        rVSelectSong.setAdapter(selectSongForPlaylistAdapter);

        //riconosce l'istanza e reinizializza l'adapter ai valori precedenti
        if(savedInstanceState !=null && savedInstanceState.containsKey("song for select")){
            ArrayList<ParcelableSong> savedSongs = savedInstanceState.getParcelableArrayList("song for select");
            ArrayList<ParcelableSong> selectedSongs = savedInstanceState.getParcelableArrayList("song selected");
            selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,savedSongs,selectedSongs);
            rVSelectSong.setAdapter(selectSongForPlaylistAdapter);
        }
        /**
         * fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Song songToEdit = EntitiesBuilder.getSong();
        Intent intent = new Intent(activity, SongCreator.class);
        intent.putExtra(SONG_TO_EDIT, (Parcelable) songToEdit);
        startActivityForResult(intent, START_EDIT_NEW_SONG);

        }
        });
         */
        /**
         * public void onClick(View v) {
         Intent intent = new Intent(activity, SelectNextSongs.class);
         intent.putExtra(PLAYLIST, (Parcelable) modifyPlaylistAdapter.getPlaylistToModify());
         startActivity(intent);
         }
         */
        /**
        try {
            Bundle boundle = getIntent().getExtras();//sono tutte le canzoni presenti nella playlist che mi passano
            playlistSongs = boundle.getParcelableArrayList("");//nome della lista che lui chiama dalla sua classe
        }
        catch(NullPointerException e){}

        dataProvider = DataProviderBuilder.getDefaultDataProvider(this);
        List<Song> songInDb = dataProvider.getSongs();//ritorna tutte le canzoni nel db

        for ( ParcelableSong i : playlistSongs) { //ogni canzone nella playlist
            if(songInDb.contains(i)) { //se presente nel db...
                songInDb.remove(i); //la rimuovo perchè non deve essere visualizzata
            }
        }
        //converto songInDb da Lista a ArrayList<ParcelableSong>
        ArrayList<ParcelableSong> songForView = new ArrayList<>();
        for (Song i: songInDb
             ) {
            songForView.add((ParcelableSong)songInDb.get(songInDb.indexOf(i)));
        }
            //songForView ora contiene tutte le canzoni presenti nel database meno quelle che sono gia presenti nella playlist


        */
        final Activity activity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for test
                Snackbar.make(view, "hai selezionato "+selectSongForPlaylistAdapter.getSelectedSongs().size()+" canzoni", Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();
                Intent intent = new Intent(activity,PlaylistView.class);
                intent.putParcelableArrayListExtra("songs_to_add",(ArrayList<ParcelableSong>) selectSongForPlaylistAdapter.getSelectedSongs());
                startActivityForResult(intent,10);
            }
        });

    }

    protected void onSaveInstanceState(Bundle outState) {
        //prende le canzoni totali
        outState.putParcelableArrayList("song for select", (ArrayList<ParcelableSong>) selectSongForPlaylistAdapter.getArraySongs());
        //prende le istanze delle canzoni già selezionate
        outState.putParcelableArrayList("song selected",(ArrayList<ParcelableSong>)selectSongForPlaylistAdapter.getSelectedSongs());

        super.onSaveInstanceState(outState);
    }


    //classe di test, al posto di questa ci sarà il database
    public ArrayList<ParcelableSong> provaDiTest(){
        ArrayList<ParcelableSong> array = new ArrayList<>();

        Song s0 = EntitiesBuilder.getSong("prova di canzone 0");
        Song s1 = EntitiesBuilder.getSong("prova di canzone 1");
        Song s2 = EntitiesBuilder.getSong("prova di canzone 2");
        Song s3 = EntitiesBuilder.getSong("prova di canzone 3");
        Song s4 = EntitiesBuilder.getSong("prova di canzone 4");
        Song s5 = EntitiesBuilder.getSong("prova di canzone 5");
        Song s6 = EntitiesBuilder.getSong("prova di canzone 6");
        Song s7 = EntitiesBuilder.getSong("prova di canzone 7");
        Song s8 = EntitiesBuilder.getSong("prova di canzone 8");
        Song s9 = EntitiesBuilder.getSong("prova di canzone 9");
        Song s10 = EntitiesBuilder.getSong("prova di canzone 10");
        Song s11 = EntitiesBuilder.getSong("prova di canzone 11");
        Song s12 = EntitiesBuilder.getSong("prova di canzone 12");
        Song s13 = EntitiesBuilder.getSong("prova di canzone 13");
        Song s14 = EntitiesBuilder.getSong("prova di canzone 14");
        Song s15 = EntitiesBuilder.getSong("prova di canzone 15");
        Song s16 = EntitiesBuilder.getSong("prova di canzone 16");
        Song s17 = EntitiesBuilder.getSong("prova di canzone 17");
        Song s18 = EntitiesBuilder.getSong("prova di canzone 18");
        Song s19 = EntitiesBuilder.getSong("prova di canzone 19");
        Song s20 = EntitiesBuilder.getSong("prova di canzone 20");

        array.add((ParcelableSong) s0);
        array.add((ParcelableSong) s1);
        array.add((ParcelableSong) s2);
        array.add((ParcelableSong) s3);
        array.add((ParcelableSong) s4);
        array.add((ParcelableSong) s5);
        array.add((ParcelableSong) s6);
        array.add((ParcelableSong) s7);
        array.add((ParcelableSong) s8);
        array.add((ParcelableSong) s9);
        array.add((ParcelableSong) s10);
        array.add((ParcelableSong) s11);
        array.add((ParcelableSong) s12);
        array.add((ParcelableSong) s13);
        array.add((ParcelableSong) s14);
        array.add((ParcelableSong) s15);
        array.add((ParcelableSong) s16);
        array.add((ParcelableSong) s17);
        array.add((ParcelableSong) s18);
        array.add((ParcelableSong) s19);
        array.add((ParcelableSong) s20);

        return array;

    }

}



