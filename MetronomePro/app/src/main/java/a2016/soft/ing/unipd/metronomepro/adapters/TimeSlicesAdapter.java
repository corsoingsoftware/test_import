package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
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
    /**
     * the song to edit
     */
    private Song songToEdit;
    private ArrayList<TimeSliceState> songToEditState;
    /**
     * max width from resources
     */
    private int maxWidth;
    /**
     * height of items
     */
    private int itemsHeigth;
    /**
     * min width from resources
     */
    private int minWidth;
    /**
     * min bpm of track, it must be refresh on every track edit
     */
    private int minBPM;
    /**
     * max bpm of track, it must be refresh on track edit
     */
    private int maxBPM;
    /**
     * min bits of track
     */
    private int minBITS;
    /**
     * max bit of tracks
     */
    private int maxBITS;
    /**
     * The revision of maximum and minimum of color and width
     */
    private int actualRevision;

    private ArrayList<OnTimeSliceSelectedListener> onTimeSliceSelectedListeners;

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener){
        this(context,dragListener, EntitiesBuilder.getSong("default-name"));

    }

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener, Song song) {
        this.dragListener=dragListener;
        this.context=context;
        this.songToEdit=song;
        this.actualRevision=0;
        this.onTimeSliceSelectedListeners= new ArrayList<>();
        calculateAllWidthsAndColors();
    }

    private void calculateAllWidthsAndColors() {
        //It must be called one time, to not decrease performance too much
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        minWidth = (int)(context.getResources().getDimension(R.dimen.min_item_ts_width)*displayMetrics.density + 0.5);
        maxWidth = (int)(context.getResources().getDimension(R.dimen.max_item_ts_width)*displayMetrics.density + 0.5);
        minBPM=minBITS=Integer.MAX_VALUE;
        maxBPM=maxBITS=Integer.MIN_VALUE;
        actualRevision++;
        songToEditState=new ArrayList<>(songToEdit.size());
        for (TimeSlice ts :
                songToEdit) {
            int bpm=ts.getBpm();
            int bits=ts.getDurationInBeats();
            if(bits>maxBITS) maxBITS=bits;
            if(bits<minBITS) minBITS=bits;
            if(bpm>maxBPM) maxBPM=bpm;
            if(bpm<minBPM) minBPM=bpm;
            //initialized with 0
            songToEditState.add(new TimeSliceState());
        }
    }

    private void onTimeSliceSelected(TimeSlice ts, int position) {
        Iterator<OnTimeSliceSelectedListener> iterator= onTimeSliceSelectedListeners.iterator();
        while(iterator.hasNext()) {
            OnTimeSliceSelectedListener current=iterator.next();
            if(current!=null){
                try{
                    current.onTimeSliceSelected(ts,position);
                }catch (Exception ex){
                    //unhandledException from listener
                }
            }else{
                iterator.remove();
            }
        }
    }

    public void addOnTimeSliceSelectedListener(OnTimeSliceSelectedListener onTimeSliceSelectedListener) {
        this.onTimeSliceSelectedListeners.add(onTimeSliceSelectedListener);
    }

    public void addTimeSlice(int position, TimeSlice newTS) {

    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        //Unregister from listeners
        holder.itemView.setOnClickListener(null);
        holder.motionView.setOnTouchListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(songToEdit,fromPosition,toPosition);
        Collections.swap(songToEditState,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        //Maybe some action with swipe like a timeslice remove
        removeTimeSlice(position);
        notifyItemRemoved(position);

    }

    /**
     * remove time slice and check parameters
     * @param position
     */
    private void removeTimeSlice(int position){
        songToEdit.remove(position);
        songToEditState.remove(position);
        //check max and min
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
        TimeSliceState tSState=songToEditState.get(position);
        if(tSState.timeSliceRevision<actualRevision){
            refreshTimeSliceState(tSState, ts);
        }
        //holder.itemView.setBackgroundColor(tSState.timeSliceColor);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(tSState.timeSliceWidth,itemsHeigth));
        holder.bitTextView.setText(Long.toString(ts.getDurationInBeats()));
        holder.bpmTextView.setText(Integer.toString(ts.getBpm()));
        //At the moment i will not visualize metric cause it doesn't count
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

    /**
     * refresh current time slice state
     * @param timeSliceState the old state
     * @param timeSliceAssociated the time slice associated
     */
    private void refreshTimeSliceState(TimeSliceState timeSliceState, TimeSlice timeSliceAssociated) {
        timeSliceState.timeSliceRevision=actualRevision;
        timeSliceState.timeSliceWidth=(((maxWidth-minWidth)*(timeSliceAssociated.getDurationInBeats()-minBITS))/
                (maxBITS-minBITS))+minWidth;
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
        //I must notify the collection changed
        this.notifyDataSetChanged();
    }

    /**
     * ViewHolder for recycler view, it can be recycled
     */
    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        //not private to direct access
        TextView bpmTextView, bitTextView, rhythmicsTextView;
        View motionView;

        ViewHolder(View itemView, TextView bpmTextView, TextView bitTextView, TextView rhythmicsTextView, View motionView) {
            super(itemView);
            this.bpmTextView = bpmTextView;
            this.bitTextView = bitTextView;
            this.motionView = motionView;
            this.rhythmicsTextView = rhythmicsTextView;
        }


        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    /**
     * This class has some additional info for time slices
     * This method needs more memory, but prevent a lot of calcs real time while user scroll the listView
     */
    static class TimeSliceState {
        /**
         * if a time slice change the max or min of something in song, all tme slices must be refresh,
         * it's a very large cost to do so every time, so with revision I can refresh just few additional TimeSliceStates,
         * So i will refresh only visible timeslices
         */
        int timeSliceRevision;
        /**
         * The width calculated for TimeSlice
         */
        int timeSliceWidth;
        /**
         * The timeSlice color calculated in timeSliceRevision
         */
        int timeSliceColor;

        public TimeSliceState(int timeSliceRevision, int timeSliceWidth, int timeSliceColor) {
            this.timeSliceRevision = timeSliceRevision;
            this.timeSliceWidth = timeSliceWidth;
            this.timeSliceColor = timeSliceColor;
        }

        /**
         * Initialize with default values: 0,0,BLACK
         */
        public TimeSliceState(){
            this(0,0, Color.BLACK);
        }
    }
}
