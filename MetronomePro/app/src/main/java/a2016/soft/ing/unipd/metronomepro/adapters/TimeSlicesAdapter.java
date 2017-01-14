package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Song;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.utilities.Constants;

/**
 * Created by Federico Favotto on 12/12/2016.
 */

public class TimeSlicesAdapter extends RecyclerView.Adapter<TimeSlicesAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private static final int DEF_COLOR = 0xFFFFFFFF;
    /**
     * It must be final cause can't change
     */
    private final OnStartDragListener dragListener;
    private final int[] timeSliceBackgroundColors;
    private Context context;
    /**
     * The current selected timeslice
     */
    private TimeSlice timeSliceSelected;
    /**
     * the song to edit
     */
    private TimeSlicesSong songToEdit;
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
     * min length of the slices
     */
    private double minLengthInSecs;
    /**
     * max length of the slices
     */
    private double maxLengthInSecs;
    /**
     * The revision of maximum and minimum of color and width
     */
    private int actualRevision;

    private ArrayList<OnTimeSliceSelectedListener> onTimeSliceSelectedListeners;

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener) {
        this(context, dragListener, EntitiesBuilder.getSong("default-name"));

    }

    public TimeSlicesAdapter(Context context, OnStartDragListener dragListener, Song song) {
        this.dragListener = dragListener;
        this.context = context;
        this.songToEdit = (TimeSlicesSong) song;
        this.actualRevision = 0;
        this.onTimeSliceSelectedListeners = new ArrayList<>();
        Resources res = context.getResources();
        TypedArray colors = res.obtainTypedArray(R.array.bPMColors);
        int color = colors.getColor(0, 0);
        timeSliceBackgroundColors = new int[colors.length()];
        for (int i = 0; i < timeSliceBackgroundColors.length; i++) {
            timeSliceBackgroundColors[i] = colors.getInt(i, DEF_COLOR);
        }
        calculateAllWidthsAndColors();
    }

    private void calculateAllWidthsAndColors() {
        //It must be called one time, to not decrease performance too much
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        itemsHeigth = (int) (150.0 * displayMetrics.density + 0.5);
        minWidth = (int) (context.getResources().getDimension(R.dimen.min_item_ts_width) * displayMetrics.density + 0.5);
        maxWidth = (int) (context.getResources().getDimension(R.dimen.max_item_ts_width) * displayMetrics.density + 0.5);
        minBPM = Integer.MAX_VALUE;
        maxBPM = Integer.MIN_VALUE;
        minLengthInSecs = Double.MAX_VALUE;
        maxLengthInSecs = Double.MIN_VALUE;
        actualRevision++;
        songToEditState = new ArrayList<>(songToEdit.size());
        for (TimeSlice ts :
                songToEdit) {
            int bpm = ts.getBpm();
            double lenght = ts.getDurationInBeats() * ((double) bpm / Constants.SECS_IN_MIN);
            if (lenght > maxLengthInSecs) maxLengthInSecs = lenght;
            if (lenght < minLengthInSecs) minLengthInSecs = lenght;
            if (bpm > maxBPM) maxBPM = bpm;
            if (bpm < minBPM) minBPM = bpm;
            //initialized with 0
            songToEditState.add(new TimeSliceState());
        }
    }

    private void onTimeSliceSelected(TimeSlice ts, int position) {
        int oldPosition = songToEdit.indexOf(timeSliceSelected);
        timeSliceSelected = ts;
        notifyItemChanged(oldPosition);
        notifyItemChanged(position);
        Iterator<OnTimeSliceSelectedListener> iterator = onTimeSliceSelectedListeners.iterator();
        while (iterator.hasNext()) {
            OnTimeSliceSelectedListener current = iterator.next();
            if (current != null) {
                try {
                    current.onTimeSliceSelected(ts, position);
                } catch (Exception ex) {
                    //unhandledException from listener
                }
            } else {
                iterator.remove();
            }
        }
    }

    public void setSelectedItem(TimeSlice timeSlice) {

    }

    public TimeSlice getTimeSliceSelected() {
        return timeSliceSelected;
    }

    public void addOnTimeSliceSelectedListener(OnTimeSliceSelectedListener onTimeSliceSelectedListener) {
        this.onTimeSliceSelectedListeners.add(onTimeSliceSelectedListener);
    }

    public void addTimeSlice(int position, TimeSlice newTS) {
        songToEdit.add(position,newTS);
        calculateAllWidthsAndColors();
        notifyDataSetChanged();
        //notifyItemInserted(position);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        //Unregister from listeners to recycle view
        holder.itemView.setOnClickListener(null);
        holder.motionView.setOnTouchListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(songToEdit, fromPosition, toPosition);
        Collections.swap(songToEditState, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        //Maybe some action with swipe like a timeslice remove
        removeTimeSlice(position);
        notifyItemRemoved(position);

    }

    /**
     * remove time slice and check parameters
     *
     * @param position
     */
    private void removeTimeSlice(int position) {
        songToEdit.remove(position);
        songToEditState.remove(position);
        //check max and min
        calculateAllWidthsAndColors();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slice_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v, (TextView) v.findViewById(R.id.bpm_text_view),
                (TextView) v.findViewById(R.id.bit_text_view),
                (TextView) v.findViewById(R.id.metric_text_view),
                v.findViewById(R.id.handle),
                v.findViewById(R.id.view_to_color));
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TimeSlice ts = songToEdit.get(position);
        TimeSliceState tSState = songToEditState.get(position);
        if (tSState.timeSliceRevision < actualRevision) {
            refreshTimeSliceState(tSState, ts);
        }
        holder.viewToColor.setBackgroundColor(tSState.timeSliceColor);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(tSState.timeSliceWidth, itemsHeigth));
        if (timeSliceSelected == ts) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.timeSliceSelectedColor));
        } else {
            holder.itemView.setBackgroundColor(0x00000000);
        }
        holder.bitTextView.setText(Long.toString(ts.getDurationInBeats()));
        holder.bpmTextView.setText(Integer.toString(ts.getBpm()));
        //At the moment i will not visualize metric cause it doesn't count
        holder.rhythmicsTextView.setText("");//ts.getTimeFigureNumerator() + "/" + ts.getTimeFigureDenominator());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSliceSelected(ts, position);
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
     *
     * @param timeSliceState      the old state
     * @param timeSliceAssociated the time slice associated
     */
    private void refreshTimeSliceState(TimeSliceState timeSliceState, TimeSlice timeSliceAssociated) {
        timeSliceState.timeSliceRevision = actualRevision;
        int colorIndex = 0;
        int span = (maxBPM - minBPM);
        int bpm = timeSliceAssociated.getBpm();
        double unit = span / (timeSliceBackgroundColors.length - 1);
        colorIndex = (int) (((bpm - minBPM) / unit));
        if (colorIndex < timeSliceBackgroundColors.length)
            timeSliceState.timeSliceColor = timeSliceBackgroundColors[colorIndex];
        else //saturation of array
            timeSliceState.timeSliceColor = timeSliceBackgroundColors[colorIndex - 1];
        span = maxWidth - minWidth;
        double lengthInSec = timeSliceAssociated.getDurationInBeats() * (bpm / Constants.SECS_IN_MIN);
        double singleTSLength = span / (maxLengthInSecs - minLengthInSecs);
        timeSliceState.timeSliceWidth = minWidth + (int) ((lengthInSec - (double) minLengthInSecs) * singleTSLength + 0.5);
    }

    @Override
    public int getItemCount() {
        return songToEdit.size();
    }

    /**
     * Return the current song to edit...
     * @return the song
     */
    public Song getSongToEdit() {
        return songToEdit;
    }

    /**
     * Change dataset
     * @param songToEdit
     */
    public void setSongToEdit(Song songToEdit) {
        this.songToEdit = (TimeSlicesSong) songToEdit;
        timeSliceSelected=null;
        calculateAllWidthsAndColors();
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
        View viewToColor;

        ViewHolder(View itemView, TextView bpmTextView, TextView bitTextView, TextView rhythmicsTextView, View motionView, View viewToColor) {
            super(itemView);
            this.bpmTextView = bpmTextView;
            this.bitTextView = bitTextView;
            this.motionView = motionView;
            this.rhythmicsTextView = rhythmicsTextView;
            this.viewToColor = viewToColor;
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
        public TimeSliceState() {
            this(0, 0, Color.BLACK);
        }
    }
}
