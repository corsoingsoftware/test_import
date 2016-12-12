package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;

import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.Song;

/**
 * Created by feder on 12/12/2016.
 */

public class TimeSlicesAdapter extends RecyclerView.Adapter<TimeSlicesAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    /**
     * It must be final cause can't change
     */
    private final OnStartDragListener dragListener;
    private Song songToEdit;


    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener){
        this(context,dragListener,null);
    }

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener, Song song) {
        this.dragListener=dragListener;
        this.context=context;
        this.songToEdit=song;
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        //Unregister from listeners
        //holder.itemView.setOnTouchListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(songToEdit,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        //Maybe some action with swipe
        notifyItemChanged(position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return songToEdit.size();
    }

    public Song getSongToEdit() {
        return songToEdit;
    }

    public void setSongToEdit(Song songToEdit) {
        this.songToEdit = songToEdit;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
