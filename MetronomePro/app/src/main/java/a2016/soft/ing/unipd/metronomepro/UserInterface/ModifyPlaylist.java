package a2016.soft.ing.unipd.metronomepro.UserInterface;

/**
 * Created by Francesco on 11/12/2016.
 */

public class ModifyPlaylist {
    private String songName;

    public ModifyPlaylist(String nameSong){
        this.songName = nameSong;
    }

    public String getTitle(){
        return songName;
    }

    public  void setTitle(String name){
        this.songName = name;
    }


}
