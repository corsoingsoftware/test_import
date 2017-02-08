package a2016.soft.ing.unipd.metronomepro;

import java.util.Random;

import a2016.soft.ing.unipd.metronomepro.entities.EntitiesBuilder;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableMidiSong;
import a2016.soft.ing.unipd.metronomepro.entities.ParcelableTimeSlicesSong;
import a2016.soft.ing.unipd.metronomepro.entities.TimeSlice;

/**
 * Created by Federico Favotto on 06/02/2017.
 */

import org.junit.Test;

public class TestGenerators {

    private static int incrementId=0;

    /**
     * It will returns a new random generated song with at least maxTimeSlices timeslices
     */
    public static ParcelableTimeSlicesSong generateRandomTestSong(int maxTimeSlices) {
        Random rand= new Random(System.currentTimeMillis());
        ParcelableTimeSlicesSong song= (ParcelableTimeSlicesSong) EntitiesBuilder.getTimeSlicesSong();
        song.setName(Integer.toString(incrementId));
        song.setId(incrementId);
        incrementId++;
        for(int i=0;i<maxTimeSlices;i++) {
            TimeSlice timeSliceToAdd= new TimeSlice();
            timeSliceToAdd.setBpm(rand.nextInt(270)+30);
            timeSliceToAdd.setDurationInBeats(rand.nextInt(2000));
            timeSliceToAdd.setTimeFigureDenominator(rand.nextInt(8));
            timeSliceToAdd.setTimeFigureDenominator(rand.nextInt(4));
            song.add(timeSliceToAdd);
        }
        return song;
    }

    public static ParcelableMidiSong generateRandomTestMidiSong() {
        Random rand = new Random(System.currentTimeMillis());
        ParcelableMidiSong midiToReturn= (ParcelableMidiSong) EntitiesBuilder.getMidiSong();
        midiToReturn.setId(incrementId);
        midiToReturn.setName(Integer.toString(incrementId));
        midiToReturn.setPath("path"+incrementId);
        incrementId++;
        return midiToReturn;
    }
}
