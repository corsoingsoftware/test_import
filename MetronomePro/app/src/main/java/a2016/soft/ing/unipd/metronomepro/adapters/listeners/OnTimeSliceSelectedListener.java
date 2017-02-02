package a2016.soft.ing.unipd.metronomepro.adapters.listeners;

import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

/**
 * Created by Federico Favotto on 12/12/2016.
 */

public interface OnTimeSliceSelectedListener {
    /**
     * Called when time slice selected change
     * @param timeSlice the new timeSlice
     * @param position the position
     */
    void onTimeSliceSelected(TimeSlice timeSlice, int position);
}
