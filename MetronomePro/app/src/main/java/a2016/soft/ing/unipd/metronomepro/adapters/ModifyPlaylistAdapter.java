package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Francesco on 12/12/2016 thanks to Federico Favotto for the great help.
 */

/**
 * This class is used to link the activity ModifyPlaylist to the rest of the project, it manage the draglistener and the interaction with
 * the user like touch, scroll, drag and drop, swipe. You can find the class of the listener in packages: touch helpers and listener
 * (created by Federico Favotto).
 * The class can modify the passed playlist.
 * We decided to use the recycler view to show the songs, here some documentation:
 * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html
 * https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html
 * https://developer.android.com/training/material/lists-cards.html
 */


public class ModifyPlaylistAdapter extends RecyclerView.Adapter<ModifyPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ParcelablePlaylist playlistToModify;
    private final OnStartDragListener dragListener;
    private Context context;
    private Song songSelected;
/**
 * default constructor
 */
    public ModifyPlaylistAdapter(ParcelablePlaylist playlistToModify, Context c, OnStartDragListener dragListener) {
        this.playlistToModify = playlistToModify;
        this.dragListener = dragListener;
        this.context = c;
    }

    public interface ModifyPlaylistAdapterCallback {
        void serviceConnected();
    }

    /**
     * add a song to the playlist
     * @param song to add
     */
    public void addSong(Song song) {
        playlistToModify.add(song);
        notifyItemInserted(playlistToModify.size()-1);
    }

    /**
     * this method was created by Giulio Pettenuzzo
     * add all the songs passed to the playlist
     * @param lista of songs to add
     */
    public void addAllSongs(ArrayList<Song> lista){
        playlistToModify.addAll(lista);
       // notifyItemInserted(playlistToModify.size()-1);
    }

    /**
     * this method was created by Giulio Pettenuzzo
     * get all the songs in the playlist
     * @return arrayList of all the songs
     */
    public ArrayList<Song> getAllSongs(){
        ArrayList<Song> parceArray = new ArrayList<>();
        for (Song s :playlistToModify) {
            parceArray.add((Song) s);
        }
        return parceArray;
    }

    /**
     * delete a passed song
     * @param song to delete
     */
        public void delete(Song song) {
        playlistToModify.remove(song);
    }

    /**
     * get the playlist that is being modify
     * @return playlist to modify
     */
    public ParcelablePlaylist getPlaylistToModify() {
        return playlistToModify;
    }

     /**
      * notice if that a song has been moved
     * notify and change its position.
     * @param fromPosition the starting position
     * @param toPosition the final position
     */
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(playlistToModify, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    /**
     * notice if a song has been swiped
     * notify and delete it.
     * @param position the position of swiped element
     */
    @Override
    public void onItemSwiped(int position) {
        playlistToModify.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.modify_playlist_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v, (TextView) v.findViewById(R.id.song_text_view), v.findViewById((R.id.handle)));
        return vh;
    }

    /**
     * when a viewHolder need to be rebuild instead of create it from the start, i just recycle an old viewHolder
     * i set to null the touchListener of the viewHolder and give the same viewHolder to the constructor
     * @param holder viewHolder to be recycled
     */
    @Override
    public void onViewRecycled(ViewHolder holder) {
        //deregistrati dagli eventi
        holder.itemView.setOnClickListener(null);
        holder.motionView.setOnTouchListener(null);
        super.onViewRecycled(holder);
    }

    /**
     * this set text of the ViewHolder to the title song, set the touch Listener to the viewHolder
     * and start the drag listener if the user do an action
     * @param holder ViewHolder to bind
     * @param position of the song inside the playlist
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Song s = playlistToModify.get(position);
        holder.songTitle.setText(s.getName());
        holder.motionView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    /**
     * get the number of the songs into the playlist that is being modify
     * @return size of the playlist
     */
    @Override
    public int getItemCount() {
        return playlistToModify.size();
    }

    /**
     * this class is used to create the viewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView songTitle;
        View motionView;

        /**
         * the constructor
         * @param itemView the view to set
         * @param songTitle the text to show in the view that is the song title
         * @param motionView
         */
        ViewHolder(View itemView, TextView songTitle, View motionView) {
            super(itemView);
            this.songTitle = songTitle;
            this.motionView = motionView;
        }

        /**
         * these two methods are override in the upper class, i need to put the just to implements the interface
         */

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
