package a2016.soft.ing.unipd.metronomepro.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelablePlaylist;
import a2016.soft.ing.unipd.metronomepro.entities.Playlist;

/**
 * Created by giuli on 13/12/2016.
 */

public class SelectPlaylistAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Playlist> arrayPlaylist;
    private int selectPlaylist;

    public SelectPlaylistAdapter(Context context) {
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_viewer, parent, false);
        RecyclerView.ViewHolder vh = new RecyclerView.ViewHolder(v,(TextView)v.findViewById(R.id.recycler_view_playlist_viewer)) {
            @Override
            public String toString() {
                return super.toString();
            }
        };

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Playlist s = arrayPlaylist.get(position);
        holder.setText(s.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
