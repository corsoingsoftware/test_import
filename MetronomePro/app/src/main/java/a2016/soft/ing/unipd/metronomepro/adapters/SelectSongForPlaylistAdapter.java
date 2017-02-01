package a2016.soft.ing.unipd.metronomepro.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.SelectSongForPlaylist;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by giuli on 27/12/2016.
 */

public class SelectSongForPlaylistAdapter extends RecyclerView.Adapter<a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter.ViewHolder>  {

    private ArrayList<Song> arraySongs; //it rappresent the songs that are already not into a playlist
    private ArrayList<Song> selectedSongs=new ArrayList<>(); //it rappresent the songs that the users want to insert into the playlist
    private Context context;

    //base constructor
    public SelectSongForPlaylistAdapter(Context context, ArrayList<Song> arraySongs){
        this.context = context;
        this.arraySongs = arraySongs;
    }
    //constructor that allows to save the instance of the songs selected
    public SelectSongForPlaylistAdapter(Context context, ArrayList<Song> arraySongs,ArrayList<Song> selectedSongs){
        this.context = context;
        this.arraySongs = arraySongs;
        this.selectedSongs = selectedSongs;
    }

    public ArrayList<Song> getArraySongs(){
        return arraySongs;
    }
    public ArrayList<Song> getSelectedSongs(){
        return selectedSongs;
    }
    public void addSong(Song song){
        arraySongs.add(song);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_song_for_playlist_item, parent, false);
        ViewHolder vh = new ViewHolder(v, (TextView)v.findViewById(R.id.item_song_for_playlist));
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Song song = arraySongs.get(position);
        holder.nameOfSong.setText(song.getName());

        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when the item is deselecting
                if(v.isSelected()){
                    v.setSelected(false); //set the background golour

                    Song song = arraySongs.get(position); //remove the songe from selected songs
                    selectedSongs.remove(song);
                    //for debug
                    Snackbar.make(v, "hai rimosso l'elemento "+ song.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                //when the item is selecting
                else{
                    v.setSelected(true);//set the background colour

                    Song song = arraySongs.get(position); //add the song to selected songs
                    selectedSongs.add(song);
                    //for debug
                    Snackbar.make(v, "hai inserito l'elemento "+song.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }
        };
        //the listener is activate when the item is building
        holder.itemView.setOnClickListener(a);
        //when an item is recycled it take the settings of the first item in the list
        //if the first item is selected, the recycle item will take the background of the first item whitch is wrong
        //without this if-else, when you press an item more than one item will higtlight
        if(selectedSongs.lastIndexOf(song)>=0){
            holder.itemView.setSelected(true);
        }
        else{
            holder.itemView.setSelected(false);
        }

    }
    //return the size of the item into the recycleview
    @Override
    public int getItemCount() {
        return arraySongs.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        //when you scroll up the list, the listener of the new item is disactivate
        //so they will set in the right settings
        holder.itemView.setOnClickListener(null);
    }

    //it describe an item view and metadata about its place within the recycleView
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nameOfSong;
        View itemView;

        public ViewHolder(View itemView, TextView nameOfSong) {
            super(itemView);
            this.itemView=itemView;
            this.nameOfSong=nameOfSong;

        }
    }
}

