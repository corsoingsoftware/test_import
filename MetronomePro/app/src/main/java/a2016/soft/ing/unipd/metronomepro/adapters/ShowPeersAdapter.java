package a2016.soft.ing.unipd.metronomepro.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.group3.sync.Peer;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.R;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;
import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperViewHolder;

/**
 * Created by Francesco on 08/02/2017.
 */

public class ShowPeersAdapter extends RecyclerView.Adapter<ShowPeersAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<Peer> peerList;

    public ShowPeersAdapter(ArrayList<Peer> peerList) {
        this.peerList = peerList;
    }

    public ShowPeersAdapter() {
        super();
    }

    @Override
    public int getItemCount() {
        return peerList.size();
    }

    public void addPeer(Peer peerToAdd) {
        peerList.add(peerToAdd);
        notifyItemInserted(peerList.size() - 1);
    }

    public void addPeer(int index, Peer peerToAdd) {
        peerList.add(index, peerToAdd);
        notifyItemInserted(index);
    }

    public void removePeer(Peer peerToRemove) {
        peerList.remove(peerToRemove);
        notifyItemRemoved(peerList.indexOf(peerToRemove));
    }

    public void removePeer(int indexOfPeerToRemove) {
        peerList.remove(indexOfPeerToRemove);
        notifyItemRemoved(indexOfPeerToRemove);
    }

    public Peer getPeer(int index) {
        return peerList.get(index);
    }

    public ArrayList getList() {
        return peerList;
    }

    public int getIndexOf(Peer peer) {
        return peerList.indexOf(peer);
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwiped(int position) {

    }

    @Override
    public ShowPeersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_show_peers, parent, false);
        ViewHolder vh = new ViewHolder(v, (TextView) v.findViewById(R.id.peer_item));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Peer p = peerList.get(position);
        holder.peerName.setText(p.getName());
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        TextView peerName;

        public ViewHolder(View itemView, TextView peerName) {
            super(itemView);
            this.peerName = peerName;
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }
}
