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
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;

/**
 * Created by giuli on 27/12/2016.
 */

    public class SelectSongForPlaylistAdapter extends RecyclerView.Adapter<a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter.ViewHolder>  {

        private ArrayList<ParcelableSong> arraySongs;
        private ArrayList<ParcelableSong> selectedSongs=new ArrayList<>(); //lista con canzoni gia selezionate: non va inizializzata qui
        private Context context;

        //costruttore: non riceve l'ascoltatore come parametro
        public SelectSongForPlaylistAdapter(Context context, ArrayList<ParcelableSong> arraySongs){
            this.context = context;
            this.arraySongs = arraySongs;
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView nameOfSong;
            private SparseBooleanArray selectedItems = new SparseBooleanArray();

            public ViewHolder(View itemView,TextView nameOfSong) {
                super(itemView);
                itemView.setOnClickListener(this);
                this.nameOfSong=nameOfSong;
            }

        @Override
        public void onClick(View v) {
            //c'è un array di booleani che tiene conto degli item che sono già stati premuti
            //questo if-else permette di selezionare e deselezionare una canzone
            if(selectedItems.get(getAdapterPosition(),false)){
                selectedItems.delete(getAdapterPosition());
                v.setSelected(false);
            }
            else{
                selectedItems.put(getAdapterPosition(),true);
                v.setSelected(true);
            }
        }
    }

    }
