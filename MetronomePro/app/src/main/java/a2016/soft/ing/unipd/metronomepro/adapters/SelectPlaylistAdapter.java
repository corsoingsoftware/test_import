package a2016.soft.ing.unipd.metronomepro.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.OnStartDragListener;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

/**
 * Created by giulio pettenuzzo on 13/12/2016.
 */

public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private ArrayList<Playlist> arrayPlaylist= new ArrayList<>();
    private Playlist playlistToEdit; //eventualmente..
    private OnPlaylistClickListener playlistClickListener;
    private OnStartDragListener dragListener;


    //COSTRUTTORE: RICEVE IN ENTRATA UNA LISTA DI PLAYLIST
    public SelectPlaylistAdapter(Context context, ArrayList<Playlist> arrayPlaylist, OnPlaylistClickListener playlistClickListener,OnStartDragListener dragListener){
        this.context = context;
        this.arrayPlaylist=arrayPlaylist;
        this.playlistClickListener = playlistClickListener;
        this.dragListener = dragListener;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Playlist p = arrayPlaylist.get(position);
        holder.nameOfPlaylist.setText(p.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistToEdit = arrayPlaylist.get(position);
                playlistClickListener.onPlaylistClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayPlaylist.size();


    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    @Override
    public void onItemSwiped(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delate_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.but_cancel_p);
        Button submit = (Button) dialog.findViewById(R.id.but_submit_p);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                notifyItemChanged(position);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayPlaylist.remove(position);
                notifyItemRemoved(position);
                dialog.cancel();
            }
        });
        dialog.show();

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

