package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.graphics.Color;
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

        notifyItemMoved(fromPosition, toPosition);

    }

    private void onSongPositionChange(int from, int to) {

        PlayableSong ps = arraySongs.get(from);
        arraySongs.remove(from);
        notifyItemRemoved(from);
        arraySongs.add(to, ps);
        notifyItemInserted(to);
    }

    @Override
    public void onItemSwiped(int position) {

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {

        //Unregister from listeners
        holder.itemView.setOnTouchListener(null);
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

        if(position >= selectedSongs)
            holder.nameSong.setTextColor(Color.BLUE);
        else
            holder.nameSong.setTextColor(Color.BLACK);

        Song s = arraySongs.get(position);
        holder.nameSong.setText(s.getName());

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                onSongTouch(position);
                return false;
            }
        });
    }

    private void onSongTouch(int position) {

        if(position < selectedSongs) {

            PlayableSong toMove = arraySongs.get(position);
            int i = selectedSongs;
            PlayableSong current = arraySongs.get(i);

            while(current.getPlaylistPosition() < toMove.getPlaylistPosition()) {
                i++;
            }

            selectedSongs--;
            onSongPositionChange(position, i);
        }
        else
        {

            onSongPositionChange(position, selectedSongs++);
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