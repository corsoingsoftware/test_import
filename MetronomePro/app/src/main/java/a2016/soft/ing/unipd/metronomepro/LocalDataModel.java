package a2016.soft.ing.unipd.metronomepro;

/**
 * Created by giuli on 10/12/2016.
 */

public class LocalDataModel {
    public final long id;
    public final long entryDate;

    public final String name;
    public final int bpm;
    public final int duration;

    public LocalDataModel(final long id,final long entryDate,final String name,final int bpm,final int duration){
        this.id=id;
        this.entryDate=entryDate;
        this.name=name;
        this.bpm=bpm;
        this.duration=duration;
    }
    public static LocalDataModel create(final long id,final long entryDate,final String name,final int bpm,final int duration){
        return new LocalDataModel(id,entryDate,name,bpm,duration);
    }
}
