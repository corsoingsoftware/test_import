package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

/**
 * Created by giuli on 13/12/2016.
 */

public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private ArrayList<Playlist> arrayPlaylist;
    private int selectPlaylist;
    private Playlist PlaylistToEdit;

    public SelectPlaylistAdapter(Context context, Playlist playlist) {
        this.context = context;
        PlaylistToEdit = playlist;
        arrayPlaylist = new ArrayList<>();
        selectPlaylist = 0;
        arrayPlaylist.add(selectPlaylist,playlist);
        selectPlaylist++;


    }
    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
    public void addPlaylist(Playlist playlist){
        arrayPlaylist.add(selectPlaylist,playlist);
        selectPlaylist++;
    }

    /**
     * private void onTimeSliceSelected(TimeSlice ts, int position) {
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
     *
     */
   /** private void onPlaylistSelected(Playlist pl, int position){
        Iterator<Playlist> iterator = arrayPlaylist.iterator();
        while(iterator.hasNext()){
            Playlist current = iterator.next();
            if(current!=null){
                try{
                    current.
                }
            }
        }
    }*/
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //creo una View..
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_viewer_item_layout,parent,false);
        //e la passo al ViewHolder
        ViewHolder vh = new ViewHolder(v,(TextView)v.findViewById(R.id.textViewItem));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Playlist p = arrayPlaylist.get(position);
        holder.nameOfPlaylist.setText(p.getName());

    }

    @Override
    public int getItemCount() {
        return arrayPlaylist.size();


    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(PlaylistToEdit, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        notifyItemChanged(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        TextView nameOfPlaylist;

        ViewHolder(View itemView,TextView nameOfPlaylist) {
            super(itemView);
            this.nameOfPlaylist=nameOfPlaylist;
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
