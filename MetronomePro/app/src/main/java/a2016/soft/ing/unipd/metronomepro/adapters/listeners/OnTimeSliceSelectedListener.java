package a2016.soft.ing.unipd.metronomepro.adapters.listeners;

import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

/**
 * Created by feder on 12/12/2016.
 */

public interface OnTimeSliceSelectedListener {
    void onTimeSliceSelected(TimeSlice timeSlice, int position);
}
