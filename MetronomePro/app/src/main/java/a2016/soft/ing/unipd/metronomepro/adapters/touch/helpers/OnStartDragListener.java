package a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers;

import android.support.v7.widget.RecyclerView;

/**
 * Created by feder on 12/12/2016.
 */

public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

}