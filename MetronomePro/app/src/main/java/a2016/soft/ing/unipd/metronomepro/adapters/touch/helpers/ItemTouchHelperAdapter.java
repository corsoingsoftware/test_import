package a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers;

/**
 * Created by feder on 12/12/2016.
 */

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemSwiped(int position);
}
