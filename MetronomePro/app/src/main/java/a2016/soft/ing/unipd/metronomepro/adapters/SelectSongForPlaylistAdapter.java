package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.SelectSongForPlaylist;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;

/**
 * Created by giuli on 27/12/2016.
 */

    public class SelectSongForPlaylistAdapter extends RecyclerView.Adapter<a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter.ViewHolder>  {

        private ArrayList<ParcelableSong> arraySongs;
        private ArrayList<ParcelableSong> selectedSongs=new ArrayList<>(); //lista con canzoni gia selezionate: non va inizializzata qui
        private Context context;

        //costruttore di base
        public SelectSongForPlaylistAdapter(Context context, ArrayList<ParcelableSong> arraySongs){
            this.context = context;
            this.arraySongs = arraySongs;
        }
        //costruttore per salvare l'istanza delle canzoni selezionate
        public SelectSongForPlaylistAdapter(Context context, ArrayList<ParcelableSong> arraySongs,ArrayList<ParcelableSong> selectedSongs){
            this.context = context;
            this.arraySongs = arraySongs;
            this.selectedSongs = selectedSongs;
        }
        
        public void remuveForTest(int position){
            arraySongs.remove(position);
        }
        public ArrayList<ParcelableSong> getArraySongs(){
            return arraySongs;
        }
        public ArrayList<ParcelableSong> getSelectedSongs(){
            return selectedSongs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_song_for_playlist_item, parent, false);
            ViewHolder vh = new ViewHolder(v, (TextView)v.findViewById(R.id.item_song_for_playlist));
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ParcelableSong song = arraySongs.get(position);
            holder.nameOfSong.setText(song.getName());
        }

        @Override
        public int getItemCount() {
            return arraySongs.size();
        }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView nameOfSong;
            private boolean isPresent;

            public ViewHolder(View itemView,TextView nameOfSong) {
                super(itemView);
                itemView.setOnClickListener(this);
                this.nameOfSong=nameOfSong;
            }

        @Override
        public void onClick(View v) {
            //questo if-else permette di selezionare e deselezionare una canzone
            isPresent=SelectSongForPlaylistAdapter.this.getSelectedSongs().contains(SelectSongForPlaylistAdapter.this.arraySongs.get(getAdapterPosition()));
            if(isPresent==true){
                v.setSelected(false);
                ParcelableSong song = SelectSongForPlaylistAdapter.this.arraySongs.get(getAdapterPosition());
                SelectSongForPlaylistAdapter.this.selectedSongs.remove(song);

                Snackbar.make(v, "hai rimosso l'elemento "+ song.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
            else{
                v.setSelected(true);

                ParcelableSong song = SelectSongForPlaylistAdapter.this.arraySongs.get(getAdapterPosition());
                SelectSongForPlaylistAdapter.this.selectedSongs.add(song);

                Snackbar.make(v, "hai inserito l'elemento "+song.getName(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
            //for test, funziona
           // Snackbar.make(v, "totale elementi selezionati: "+ SelectSongForPlaylistAdapter.this.selectedSongs.size(), Snackbar.LENGTH_LONG)
            //        .setAction("Action", null).show();
        }
    }

    }
