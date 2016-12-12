package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.listeners.OnTimeSliceSelectedListener;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

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

    private ArrayList<OnTimeSliceSelectedListener> onTimeSliceSelectedListeners;

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener){
        this(context,dragListener, EntitiesBuilder.getSong("default-name"));

    }

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener, Song song) {
        this.dragListener=dragListener;
        this.context=context;
        this.songToEdit=song;
        this.onTimeSliceSelectedListeners= new ArrayList<>();
    }

    private void onTimeSliceSelected(TimeSlice ts, int position) {
        Iterator<OnTimeSliceSelectedListener> iterator= onTimeSliceSelectedListeners.iterator();
        while(iterator.hasNext()) {
            OnTimeSliceSelectedListener current=iterator.next();
            if(current!=null){
                try{
                    current.onTimeSliceSelected(ts,position);
                }catch (Exception ex){
                    //unhandledException from listen
                }
            }else{
                iterator.remove();
            }
        }
    }

    public void addOnTimeSliceSelectedListener(OnTimeSliceSelectedListener onTimeSliceSelectedListener) {
        this.onTimeSliceSelectedListeners.add(onTimeSliceSelectedListener);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        //Unregister from listeners
        holder.itemView.setOnClickListener(null);
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slice_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v,(TextView)v.findViewById(R.id.bpm_text_view),
                (TextView)v.findViewById(R.id.bit_text_view),
                (TextView)v.findViewById(R.id.metric_text_view),
                v.findViewById(R.id.handle));
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TimeSlice ts=songToEdit.get(position);
        holder.bitTextView.setText(Long.toString(ts.getDurationInBeats()));
        holder.bpmTextView.setText(Integer.toString(ts.getBpm()));
        holder.rhythmicsTextView.setText(ts.getTimeFigureNumerator()+"/"+ts.getTimeFigureDenominator());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSliceSelected(ts,position);
            }
        });
        holder.motionView.setOnTouchListener(new View.OnTouchListener() {
                 @Override
                 public boolean onTouch(View v, MotionEvent event) {
                     if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                         dragListener.onStartDrag(holder);
                     }
                     return false;
                 }
             }
        );
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

        //not private to direct access
        TextView bpmTextView, bitTextView, rhythmicsTextView;
        View motionView;

        ViewHolder(View itemView, TextView bpmTextView, TextView bitTextView, TextView rhythmicsTextView, View motionView) {
            super(itemView);
            this.bpmTextView = bpmTextView;
            this.bitTextView = bitTextView;
            this.motionView=motionView;
            this.rhythmicsTextView = rhythmicsTextView;
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}