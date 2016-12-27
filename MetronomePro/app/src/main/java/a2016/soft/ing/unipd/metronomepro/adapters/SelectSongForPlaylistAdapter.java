package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;

/**
 * Created by giuli on 27/12/2016.
 */

    public class SelectSongForPlaylistAdapter extends RecyclerView.Adapter<a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter.ViewHolder> {

        private ArrayList<ParcelableSong> arraySongs;
        private ArrayList<ParcelableSong> selectedSongs; //lista con canzoni gia selezionate
        private Context context;
        private int songSelected;

        public SelectSongForPlaylistAdapter(Context context, ArrayList<ParcelableSong> arraySongs){
            this.context = context;
            this.arraySongs = arraySongs;
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

        public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

            TextView nameOfSong;

            public ViewHolder(View itemView,TextView nameOfSong) {
                super(itemView);
                this.nameOfSong=nameOfSong;
            }

            @Override
            public void onItemSelected() {

            }

            @Override
            public void onItemClear() {

            }
        }

    }
