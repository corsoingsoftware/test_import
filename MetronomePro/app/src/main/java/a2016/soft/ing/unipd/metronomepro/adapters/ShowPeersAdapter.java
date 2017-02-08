package a2016.soft.ing.unipd.metronomepro.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import org.group3.sync.Peer;

import java.util.ArrayList;

import a2016.soft.ing.unipd.metronomepro.adapters.touch.helpers.ItemTouchHelperAdapter;

/**
 * Created by Francesco on 08/02/2017.
 */

public class ShowPeersAdapter extends RecyclerView.Adapter<ModifyPlaylistAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<Peer> peerList;

    public ShowPeersAdapter(ArrayList<Peer> peerList) {
        this.peerList = peerList;
    }

    public ShowPeersAdapter() {
        super();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwiped(int position) {

    }

    @Override
    public ModifyPlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ModifyPlaylistAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return peerList.size();
    }

    public void addPeer(Peer peerToAdd){
        peerList.add(peerToAdd);
        notifyItemInserted(peerList.size()-1);
    }

    public void addPeer(int index, Peer peerToAdd){
        peerList.add(index, peerToAdd);
        notifyItemInserted(index);
    }

    public void removePeer(Peer peerToRemove){
        peerList.remove(peerToRemove);
        notifyItemRemoved(peerList.indexOf(peerToRemove));
    }

    public  void removePeer(int indexOfPeerToRemove){
        peerList.remove(indexOfPeerToRemove);
        notifyItemRemoved(indexOfPeerToRemove);
    }

    public Peer getPeer(int index){
        return peerList.get(index);
    }

    public ArrayList getList(){
        return peerList;
    }

    public int getIndexOf(Peer peer){
        return peerList.indexOf(peer);
    }

}
