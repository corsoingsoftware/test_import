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
 * Created by giuli on 27/11/2016.
 */

/**
 * SelectSongForPlaylistAdapter and SelectSongForPlaylist are built for insert the songs that are saved into the database, into a
 * playlist that is recived from an other activity.
 * This adapter allows to select and deselect the songs that will be included in the playlist
 */
public class SelectSongForPlaylistAdapter extends RecyclerView.Adapter<a2016.soft.ing.unipd.metronomepro.adapters.SelectSongForPlaylistAdapter.ViewHolder>  {

    private ArrayList<Song> arraySongs; //it rappresent the songs that the user could select
    private ArrayList<Song> selectedSongs=new ArrayList<>(); //it rappresent the songs that the users want to insert into the playlist
    private Context context; // the context of the activity

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

    /**
     * @return the songs in recycleView
     */
    public ArrayList<Song> getArraySongs(){
        return arraySongs;
    } //return the list of the all songs

    /**
     * @return the songs that user selected
     */
    public ArrayList<Song> getSelectedSongs(){//return the list of the songs Selected
        return selectedSongs;
    }

    /**
     * @param song: the song to add
     */
    public void addSong(Song song){
        arraySongs.add(song);
    }
    //this is the creator of the ViewHolder
    //see documentation for more information
    @Override
    /**
     * this is the creator of the ViewHolder
     * see documentation for more information
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewOfRecycle = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_song_for_playlist_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(viewOfRecycle, (TextView)viewOfRecycle.findViewById(R.id.item_song_for_playlist));
        return viewHolder;

    }

    @Override
    /**
     * This method internally calls onBindViewHolder(ViewHolder, int) to update the RecyclerView.ViewHolder
     * contents with the item at the given position and also sets up some private fields to be used by RecyclerView.
     * for more information: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html
     */
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Song song = arraySongs.get(position);
        holder.nameOfSong.setText(song.getName());//to see the name of the songs in the RecycleView
    //below the code for hilight an item when pressed
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when the item is deselecting
                if(v.isSelected()){
                    v.setSelected(false); //set the background golour
                    selectedSongs.remove(arraySongs.get(position));//remove the songe from selected songs
                }
                //when the item is selecting
                else{
                    v.setSelected(true);//set the background colour

                    Song song = arraySongs.get(position); //add the song to selected songs
                    selectedSongs.add(song);
                }
            }
        };
        //the listener is activate when the item is building
        holder.itemView.setOnClickListener(itemClickListener);
        //when an item is recycled it take the settings of the first item in the list
        //if the first item is selected, the recycled item will take the background of the first item whitch is wrong
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
    /**
     * this method explain what heppens when the view is recycle
     */
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        //when you scroll up the list, the listener of the new item is disactivate
        //so they will set in the right settings
        holder.itemView.setOnClickListener(null);
    }
    /**
     *A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     *for more information: https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html
     */
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

