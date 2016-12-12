package a2016.soft.ing.unipd.metronomepro.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;

/**
 * Created by feder on 12/12/2016.
 */

public class TimeSlicesAdapter extends RecyclerView.Adapter<TimeSlicesAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwiped(int position) {

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
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public ViewHolder(View itemView, TextView playerName, TextView playerPriority , ImageView handlerView) {
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
