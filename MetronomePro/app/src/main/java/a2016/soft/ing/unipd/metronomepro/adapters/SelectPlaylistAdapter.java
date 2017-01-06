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
    private ArrayList<Playlist> arrayPlaylist= new ArrayList<>();
    private int selectPlaylist;
    private Playlist PlaylistToEdit; //eventualmente..


    //COSTRUTTORE DI PROVA RICEVE UNA PLAYLIT IN INGRESSO
   /** public SelectPlaylistAdapter(Context context, Playlist playlist) {
        this.context = context;
        PlaylistToEdit = playlist;
        arrayPlaylist = new ArrayList<>();
        selectPlaylist = 0;
        arrayPlaylist.add(selectPlaylist,playlist);
        selectPlaylist++;
    }*/
   //QUESTO COSTRUTTORE RICEVE IN ENTRATA UNA LISTA DI PLAYLIST
   public SelectPlaylistAdapter(Context context, ArrayList<Playlist> arrayPlaylist){
       this.context = context;
       this.arrayPlaylist=arrayPlaylist;
       selectPlaylist=0;
   }
    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
    public void addPlaylist(Playlist playlist){
        arrayPlaylist.add(selectPlaylist,playlist);
        selectPlaylist++;
    }

    public void remuvePlaylist(int position){
        arrayPlaylist.remove(position);
        selectPlaylist--;
    }

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
    }

    @Override
    public void onItemSwiped(int position) {
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
