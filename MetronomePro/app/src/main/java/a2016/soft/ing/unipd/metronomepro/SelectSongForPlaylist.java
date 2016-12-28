package a2016.soft.ing.unipd.metronomepro;

import android.os.Bundle;
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

import a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

    public class SelectSongForPlaylist extends AppCompatActivity {

        private RecyclerView rVSelectSong;
        private RecyclerView.LayoutManager rVLayoutManager;
        private SelectSongForPlaylistAdapter selectSongForPlaylistAdapter;

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
                selectSongForPlaylistAdapter = new SelectSongForPlaylistAdapter(this,savedSongs);
                rVSelectSong.setAdapter(selectSongForPlaylistAdapter);
            }


           /**
             rVSelectSong.setOnClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(SelectSongForPlaylist.this, "", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"hai selezionato un pulsante",Toast.LENGTH_SHORT).show();
                }
            });
             */


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //test fatto male, funziona!!! salva le istanze! se giro lo schermo rimane eliminata
                    //selectSongForPlaylistAdapter.remuveForTest(0);
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        protected void onSaveInstanceState(Bundle outState) {
            //prende le canzoni totali e quelle selezionate
            outState.putParcelableArrayList("song for select", (ArrayList<ParcelableSong>) selectSongForPlaylistAdapter.getArraySongs());
            //questa è per salvare l'istanza delle canzoni già selezionate..
            //bisogna cambiare la chiave??
           // outState.putParcelableArrayList("song for select", (ArrayList<ParcelableSong>)selectSongForPlaylistAdapter.getSelectedSongs());
            super.onSaveInstanceState(outState);
        }

        public ArrayList<ParcelableSong> provaDiTest(){
            ArrayList<ParcelableSong> array = new ArrayList<>();

            Song s1 = EntitiesBuilder.getSong("prova di canzone 1");
            Song s2 = EntitiesBuilder.getSong("prova di canzone 2");
            Song s3 = EntitiesBuilder.getSong("prova di canzone 3");

            array.add((ParcelableSong) s1);
            array.add((ParcelableSong) s2);
            array.add((ParcelableSong) s3);

            return array;

        }

    }



