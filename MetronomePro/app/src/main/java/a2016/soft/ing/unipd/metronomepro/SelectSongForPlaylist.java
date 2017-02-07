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
/**
 * Created by giuli on 27/12/2016.
 */
public class SelectSongForPlaylist extends AppCompatActivity {

    private RecyclerView rVSelectSong;
    private RecyclerView.LayoutManager rVLayoutManager;
    private SelectSongForPlaylistAdapter selectSongForPlaylistAdapter;
    private DataProvider dataProvider = DataProviderBuilder.getDefaultDataProvider(this); //the database
    private ArrayList<Song> songsFrom;  //rappresent the songs recived from the activity that want to insert new songs
    ArrayList<Song> savedSongs = new ArrayList<>(); //it is used for save the instance of the activity
    ArrayList<Song> selectedSongs; //the song that the user has selected
    ArrayList<Song> songForAdapter; //this is the list of the songs used to give to the adapter constructor
    private DataProvider db = DataProviderBuilder.getDefaultDataProvider(this);

    private static final int SONG_CREATED = 1;
    private static final String SONG_SELECTING = "song for select";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_song_for_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //this is the tipical code for initializing a recycleview
        rVSelectSong = (RecyclerView)findViewById(R.id.recicle_song_for_playlist);
        rVSelectSong.setHasFixedSize(true);
        rVLayoutManager = new LinearLayoutManager(this);
        rVSelectSong.setLayoutManager(rVLayoutManager);

        for (Song s:provaDiTest()) { //save 20 songs in the database for testing
            db.saveSong(s);
        }
        songForAdapter= (ArrayList<Song>) db.getAllSongs();//in the beginning songsForAdapter contains All the songs of the Database

        //saving the instance of the song selected and deselected
        //for more information https://developer.android.com/guide/components/activities/activity-lifecycle.html
        if(savedInstanceState !=null && savedInstanceState.containsKey(SONG_TO_ADD)){//save the instance of the activity
            savedSongs = savedInstanceState.getParcelableArrayList(SONG_SELECTING);//get the parcel of the all songs in the list
            selectedSongs = savedInstanceState.getParcelableArrayList(SONG_TO_ADD);//get the parcel of the song selected
            selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,savedSongs,selectedSongs);//adapter initialized
            rVSelectSong.setAdapter(selectSongForPlaylistAdapter);//and passed to the recycleView
        }
        else{
            Intent intent=getIntent();
            if(intent!=null) {
                try {
                    songsFrom = intent.getParcelableArrayListExtra(PLAYLIST);//takes the intent from ModifyPlaylistActivity
                    //ModifyPlaylistActivity is a class that rapresent the playlist
                    //it already  contains songs..
                    //when the user inserts the new songs in the playlist, it must be able to enter only the songs that are not yet within the playlist
                    //so I take all the songs in the database and I subtract those that pass me from ModifyPlaylistActivity
                     for (int i = 0;i<songsFrom.size();i++) {
                        for (int j = 0;j<songForAdapter.size();j++) {
                            if(songsFrom.get(i).getName().compareTo(songForAdapter.get(j).getName())==0){
                                songForAdapter.remove(j);
                            }
                        }
                    }


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }


            selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,songForAdapter);//adapter initialized
            rVSelectSong.setAdapter(selectSongForPlaylistAdapter);//and passed to the recycleview
        }

        final Activity activity = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //I return with this button the songs selected by the user to ModifyPlaylistActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ModifyPlaylistActivity.class);
                intent.putParcelableArrayListExtra(SONG_TO_ADD,(ArrayList<Song>) selectSongForPlaylistAdapter.getSelectedSongs());
                //the intent now contains the songs selected by the user
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        FloatingActionButton FabtoEditorActivity = (FloatingActionButton) findViewById(R.id.FabtoEditorActivity);
        //to create a new TimeSlice song with the activity SongCreator
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
        //it takes all the songs in the adapter
        outState.putParcelableArrayList(SONG_SELECTING, (ArrayList<Song>) selectSongForPlaylistAdapter.getArraySongs());
        //it takes only the songs that user selected
        outState.putParcelableArrayList(SONG_TO_ADD,(ArrayList<Song>)selectSongForPlaylistAdapter.getSelectedSongs());

        super.onSaveInstanceState(outState);
    }

    @Override //this method takes the TimeSlice song created by SongCreator
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SONG_CREATED){
                TimeSlicesSong songCreated = data.getParcelableExtra(SONG_TO_EDIT);
                dataProvider.saveSong(songCreated);//save in database
                selectSongForPlaylistAdapter.addSong(songCreated);//add to adapter
                selectSongForPlaylistAdapter.notifyDataSetChanged();
            }
        }
    }

    //class fo test
    //20 songs will save in the database
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



