package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import a2016.soft.ing.unipd.metronomepro.ModifyPlaylistActivity;
import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

/**
 * Created by giulio pettenuzzo on 13/12/2016.
 */

public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private ArrayList<Playlist> arrayPlaylist= new ArrayList<>();
    private Playlist playlistToEdit; //eventualmente..
    private OnPlaylistClickListener playlistClickListener;


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
    public SelectPlaylistAdapter(Context context, ArrayList<Playlist> arrayPlaylist, OnPlaylistClickListener playlistClickListener){
        this.context = context;
        this.arrayPlaylist=arrayPlaylist;
        this.playlistClickListener = playlistClickListener;
    }
    //costruttore per salvare le istanzepublic
    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }
    public void addPlaylist(Playlist playlist){
        arrayPlaylist.add(arrayPlaylist.size(),playlist);
    }

    public void remuvePlaylist(int position){
        arrayPlaylist.remove(position);
    }
    public Playlist getPlaylistToEdit(){
        return playlistToEdit;
    }
    public ArrayList<Playlist> getArrayPlaylist(){
        return arrayPlaylist;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Playlist p = arrayPlaylist.get(position);
        holder.nameOfPlaylist.setText(p.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistToEdit = arrayPlaylist.get(position);
                playlistClickListener.onPlaylistClick();
            }
        });

        /**
         final View.OnClickListener a = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent(context,ModifyPlaylistActivity.class);
        intent.putExtra("playlist_selected",arrayPlaylist.get(pos));
        context.start
        }
        };*/

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
    public interface OnPlaylistClickListener{
        void onPlaylistClick();
    }
}

