package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.listeners.OnTimeSliceSelectedListener;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

/**
 * Created by Francesco on 12/12/2016.
 */

public class ModifyPlaylistAdapter extends RecyclerView.Adapter<ModifyPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Playlist playlistToModify;
    private ArrayList<Song> songList;
    private final OnStartDragListener dragListener;
    private Context context;

    public ModifyPlaylistAdapter(Playlist playlistToModify, Context c, OnStartDragListener dragListener) {
        this.playlistToModify = playlistToModify;
        this.dragListener = dragListener;
    }

    //è solo un costruttore di prova
    public ModifyPlaylistAdapter(Playlist playlistToModify, Context c, OnStartDragListener dragListener, Song song) {
        this.playlistToModify = playlistToModify;
        this.dragListener = dragListener;
        this.context = c;
        songList = new ArrayList<Song>();
        songList.add(song);
    }

    public void addSong(Song song) {
        songList.add(song);
    }

    public void delete(Song song) {
        songList.remove(song);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(playlistToModify, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        notifyItemChanged(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_layout, parent, false);
        ViewHolder vh = new ViewHolder(v, (TextView) v.findViewById(R.id.song_title_text_view));
        return vh;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        //deregistrati dagli eventi
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song s = playlistToModify.get(position);

    }

    @Override
    public int getItemCount() {
        return playlistToModify.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        TextView songTitle;

        public ViewHolder(View itemView, TextView songTitle) {
            super(itemView);
            this.songTitle = songTitle;
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
