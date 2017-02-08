package a2016.soft.ing.unipd.metronomepro.adapters;

import org.group3.sync.Peer;

import java.util.ArrayList;

/**
 * Created by Francesco on 08/02/2017.
 */

public class ShowPeersAdapter {

    private ArrayList<Peer> peerList;

    public ShowPeersAdapter(ArrayList<Peer> peerList) {
        this.peerList = peerList;
    }

    public void addPeer(Peer peerToAdd){
        peerList.add(peerToAdd);
    }

    public void removePeer(Peer peerToRemove){
        peerList.remove(peerToRemove);
    }

    public  void removePeer(int indexOfPeerToRemove){
        peerList.remove(indexOfPeerToRemove);
    }

    public ArrayList getList(){
        return peerList;
    }

    public int getSize(){
        return peerList.size();
    }

    public int getIndex(Peer peer){
        return peerList.indexOf(peer);
    }

}
