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
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;

import static a2016.soft.ing.unipd.metronomepro.ActivityExtraNames.*;

public class SelectSongForPlaylist extends AppCompatActivity {

    private RecyclerView rVSelectSong;
    private static final int SONG_CREATED = 1;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectSongForPlaylistAdapter selectSongForPlaylistAdapter;
    private DataProvider dataProvider;
    private ArrayList<Song> playlistSongs;
    ArrayList<Song> savedSongs = new ArrayList<>();
    ArrayList<Song> selectedSongs;
    ArrayList<Song> songForAdapter;
    private DataProvider db = DataProviderBuilder.getDefaultDataProvider(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song_for_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rVSelectSong = (RecyclerView)findViewById(R.id.recicle_song_for_playlist);
        rVSelectSong.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVSelectSong.setLayoutManager(rVLayoutManager);

        for (Song s:provaDiTest()) {
            db.saveSong(s);
        }
        songForAdapter= (ArrayList<Song>) db.getAllSongs();
        /**
         * if(savedInstanceState!=null&&savedInstanceState.containsKey(SONG_TO_EDIT)){
         songToEdit=savedInstanceState.getParcelable(SONG_TO_EDIT);
         }else{
         Intent intent=getIntent();
         try {
         songToEdit = intent.getParcelableExtra(SONG_TO_EDIT);
         } catch (Exception ex){
         ex.printStackTrace();
         }
         }
         timeSlicesAdapter = new TimeSlicesAdapter(this, this,songToEdit);
         rVTimeSlices.setAdapter(timeSlicesAdapter);
         HorizontalDragTouchHelperCallback myItemTouchHelper = new HorizontalDragTouchHelperCallback(timeSlicesAdapter);
         itemTouchHelper = new ItemTouchHelper(myItemTouchHelper);
         itemTouchHelper.attachToRecyclerView(rVTimeSlices);
         FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabOk);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Snackbar.make(view,getString(R.string.saved_string), Snackbar.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        ParcelableSong ps=(ParcelableSong) timeSlicesAdapter.getSongToEdit();
        returnIntent.putExtra(SONG_TO_EDIT, ps);
        setResult(RESULT_OK,returnIntent);
        finish();
        }
        });
         */

        if(savedInstanceState !=null && savedInstanceState.containsKey(SONG_TO_ADD)){
            savedSongs = savedInstanceState.getParcelableArrayList("song for select");
            selectedSongs = savedInstanceState.getParcelableArrayList(SONG_TO_ADD);
            selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,savedSongs,selectedSongs);
            rVSelectSong.setAdapter(selectSongForPlaylistAdapter);
        }
        else{
            Intent intent=getIntent();
            if(intent!=null) {
                try {
                    savedSongs = intent.getParcelableArrayListExtra(PLAYLIST);
                    for (int i = 0;i<songForAdapter.size();i++) {
                        for (int j = 0;j<savedSongs.size();j++) {
                            if(savedSongs.get(j).getName().compareTo(songForAdapter.get(i).getName())==0){
                                songForAdapter.remove(i);
                            }
                        }
                    }



                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,songForAdapter);
            rVSelectSong.setAdapter(selectSongForPlaylistAdapter);
        }



        //riconosce l'istanza e reinizializza l'adapter ai valori precedenti

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
                Intent intent = new Intent(activity,ModifyPlaylistActivity.class);
                intent.putParcelableArrayListExtra(SONG_TO_ADD,(ArrayList<Song>) selectSongForPlaylistAdapter.getSelectedSongs());
                setResult(RESULT_OK,intent);
                finish();
               // startActivityForResult(intent,RESULT_OK);
                /**
                 * Intent returnIntent = new Intent();
                 ParcelableSong ps=(ParcelableSong) timeSlicesAdapter.getSongToEdit();
                 returnIntent.putExtra(SONG_TO_EDIT, ps);
                 setResult(RESULT_OK,returnIntent);
                 finish();
                 */
            }
        });
        /**
        ParcelablePlaylist playlistToModify = new ParcelablePlaylist("giulio");
        playlistToModify.addAll(selectSongForPlaylistAdapter.getSelectedSongs());
         */
        FloatingActionButton FabtoEditorActivity = (FloatingActionButton) findViewById(R.id.FabtoEditorActivity);
        FabtoEditorActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            //se voglio modificae una song passo nel extra al posto del entities bulder
            public  void onClick(View view) {
                Intent intent = new Intent(activity,SongCreator.class);
                intent.putExtra(SONG_TO_EDIT,EntitiesBuilder.getTimeSlicesSong());
                startActivityForResult(intent,SONG_CREATED);
            }
        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        //prende le canzoni totali
        outState.putParcelableArrayList("song for select", (ArrayList<Song>) selectSongForPlaylistAdapter.getArraySongs());
        //prende le istanze delle canzoni già selezionate
        outState.putParcelableArrayList(SONG_TO_ADD,(ArrayList<Song>)selectSongForPlaylistAdapter.getSelectedSongs());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SONG_CREATED){
                TimeSlicesSong songCreated = data.getParcelableExtra(SONG_TO_EDIT);
                dataProvider.saveSong(songCreated);
                songForAdapter.add(songCreated);
            }
        }
    }

    //classe di test, al posto di questa ci sarà il database
    public ArrayList<Song> provaDiTest(){
        ArrayList<Song> array = new ArrayList<>();

        Song s0 = EntitiesBuilder.getSong("song 0");
        Song s1 = EntitiesBuilder.getSong("song 1");
        Song s2 = EntitiesBuilder.getSong("song 2");
        Song s3 = EntitiesBuilder.getSong("song 3");
        Song s4 = EntitiesBuilder.getSong("song 4");
        Song s5 = EntitiesBuilder.getSong("song 5");
        Song s6 = EntitiesBuilder.getSong("song 6");
        Song s7 = EntitiesBuilder.getSong("song 7");
        Song s8 = EntitiesBuilder.getSong("song 8");
        Song s9 = EntitiesBuilder.getSong("song 9");
        Song s10 = EntitiesBuilder.getSong("song 10");
        Song s11 = EntitiesBuilder.getSong("song 11");
        Song s12 = EntitiesBuilder.getSong("song 12");
        Song s13 = EntitiesBuilder.getSong("song 13");
        Song s14 = EntitiesBuilder.getSong("song 14");
        Song s15 = EntitiesBuilder.getSong("song 15");
        Song s16 = EntitiesBuilder.getSong("song 16");
        Song s17 = EntitiesBuilder.getSong("song 17");
        Song s18 = EntitiesBuilder.getSong("song 18");
        Song s19 = EntitiesBuilder.getSong("song 19");
        Song s20 = EntitiesBuilder.getSong("song 20");

        array.add((Song) s0);
        array.add((Song) s1);
        array.add((Song) s2);
        array.add((Song) s3);
        array.add((Song) s4);
        array.add((Song) s5);
        array.add((Song) s6);
        array.add((Song) s7);
        array.add((Song) s8);
        array.add((Song) s9);
        array.add((Song) s10);
        array.add((Song) s11);
        array.add((Song) s12);
        array.add((Song) s13);
        array.add((Song) s14);
        array.add((Song) s15);
        array.add((Song) s16);
        array.add((Song) s17);
        array.add((Song) s18);
        array.add((Song) s19);
        array.add((Song) s20);


        return array;

    }

}



