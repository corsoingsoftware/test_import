package a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers;

/**
 * Created by feder on 12/12/2016.
 */

public interface ItemTouchHelperAdapter {

    /**
     * Called when item is moved in the list
     * @param fromPosition the starting position
     * @param toPosition the final position
     */
    void onItemMove(int fromPosition, int toPosition);

    /**
     * Called when user swiped an element
     * @param position the position of swiped element
     */
    void onItemSwiped(int position);
}
