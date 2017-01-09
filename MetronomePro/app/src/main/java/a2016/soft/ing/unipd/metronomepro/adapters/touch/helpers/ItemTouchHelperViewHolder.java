package a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Federico Favotto on 12/12/2016.
 */

public interface ItemTouchHelperViewHolder {

    /**
     * Called when the {@link ItemTouchHelper} first registers an item as being moved or swiped.
     * Implementations should update the item view to indicate it's active state.
     */
    void onItemSelected();


    /**
     * Called when the {@link ItemTouchHelper} has completed the move or swipe, and the active item
     * state should be cleared.
     */
    void onItemClear();
}
