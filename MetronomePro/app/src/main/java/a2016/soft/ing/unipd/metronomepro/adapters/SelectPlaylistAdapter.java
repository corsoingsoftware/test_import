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
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProvider;
import a2016.soft.ing.unipd.metronomepro.data.access.layer.DataProviderBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

/**
 * Created by giulio pettenuzzo on 13/12/2016.
 */

/**
 *This class allows to manage playlists
 */

public class SelectPlaylistAdapter extends RecyclerView.Adapter<SelectPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private ArrayList<Playlist> arrayPlaylist= new ArrayList<>();//this is the list of playlist created
    private Playlist playlistToEdit;//the playlist the user want to edit
    private OnPlaylistClickListener playlistClickListener;
    private OnStartDragListener dragListener;//to permise to delate a playlist
    private DataProvider db;


    //COSTRUTTORE: RICEVE IN ENTRATA UNA LISTA DI PLAYLIST
    public SelectPlaylistAdapter(Context context, ArrayList<Playlist> arrayPlaylist, OnPlaylistClickListener playlistClickListener,OnStartDragListener dragListener){
        this.context = context;
        this.arrayPlaylist=arrayPlaylist;
        this.playlistClickListener = playlistClickListener;
        this.dragListener = dragListener;
        db=DataProviderBuilder.getDefaultDataProvider(context);
    }
    //costruttore per salvare le istanzepublic
    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    /**
     * method to add a new playlist
     * @param playlist
     */
    public void addPlaylist(Playlist playlist){
        arrayPlaylist.add(arrayPlaylist.size(),playlist);
    }

    /**
     * to remuve a playlist
     * @param position
     */
    public void remuvePlaylist(int position){
        arrayPlaylist.remove(position);
    }

    /**
     * to get the playlist the user select
     * @return
     */
    public Playlist getPlaylistToEdit(){
        return playlistToEdit;
    }

    /**
     * to get the whole playlist
     * @return
     */
    public ArrayList<Playlist> getArrayPlaylist(){
        return arrayPlaylist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //creating a view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_viewer_item_layout,parent,false);
        //and pass it to view holder
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

    /**
     * @return the total number of playlist
     */
    @Override
    public int getItemCount() {
        return arrayPlaylist.size();
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    /**
     * when a item is swipped: a dialog will be open and the user can decide if delate it or not
     * @param position the position of swiped element
     */
    @Override
    public void onItemSwiped(final int position) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.delate_dialog);
        Button cancel = (Button) dialog.findViewById(R.id.but_cancel_p);//not delate
        Button submit = (Button) dialog.findViewById(R.id.but_submit_p);//delate
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
                db.deletePlaylist(arrayPlaylist.remove(position));
                notifyDataSetChanged();
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