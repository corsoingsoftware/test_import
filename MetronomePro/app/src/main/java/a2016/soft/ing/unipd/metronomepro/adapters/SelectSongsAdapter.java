package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.PlayableSong;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by Omar on 12/12/2016.
 */

public class SelectSongsAdapter extends RecyclerView.Adapter<SelectSongsAdapter.ViewHolder> implements ItemTouchHelperAdapter{


    private ArrayList<PlayableSong> arraySongs;
    private Context context;
    private int selectedSongs;
    private int maxSelectable;

    public SelectSongsAdapter(Context context, Playlist p, int selectedSongs, int maxSelectable){

        this.context = context;
        arraySongs = new ArrayList<>(p.size());
        int i = 0;
        for (Song s :
                p) {
            arraySongs.add(new PlayableSong(s, i++, 0));
        }

        this.selectedSongs = selectedSongs;
        this.maxSelectable = maxSelectable;

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    private void onSongPositionChange(int from, int to) {

        if(from < to) {
            for(int i = from+1; i < to; i++) {
                Collections.swap(arraySongs, i, i-1);
            }
        }
        else
        {
            for(int i = from-1; i > to; i--) {
                Collections.swap(arraySongs, i, i+1);
            }
        }

        notifyItemRangeChanged(Math.min(from,to), Math.abs(from-to));
    }

    @Override
    public void onItemSwiped(int position) {

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {

        //Unregister from listeners
        super.onViewRecycled(holder);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.next_song_item_layout, parent, false);
        ViewHolder vh = new ViewHolder(v, (TextView)v.findViewById(R.id.song_title_text_view));

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Song s = arraySongs.get(position);
        holder.nameSong.setText(s.getName());
       /* holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                onSongTouch(position);
                return false;
            }
        });*/
    }

    private void onSongTouch(int position) {

        if(position < selectedSongs) {
            selectedSongs--;
            onItemMove(position, arraySongs.get(position).getPlaylistPosition());
        }
        else
        {
            onItemMove(position, selectedSongs++);
        }
    }

    @Override
    public int getItemCount() {
        return arraySongs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        TextView nameSong;

        public ViewHolder(View itemView, TextView nameSong) {
            super(itemView);
            this.nameSong = nameSong;
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}