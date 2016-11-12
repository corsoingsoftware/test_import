package a2016.soft.ing.unipd.metronomepro;

import android.widget.TextView;

/**
 * Created by Francesco on 12/11/2016.
 *
 * in questa classe pensavo di creare i metodi per gestire la ritmica.
 * Il metronomo dovà fare una serie di TOC TOC TOC prima di un suono diverso (possibilmente più alto) TIC
 * In pratica l'utente selezione dall'interfaccia una ritmica precsa (4/4, 3/4, 5/8 ecc...)
 * un metodo ricevera in ingresso il parametro e farà il conto di quanti TOC fare pria di cambiare suono a TIC
 * un altro si occuperò di mandare l'informazione al suond manager per fare in modo che la traccia creata abbia già la ritmica coretta.
 */

public class GestoreRitmica {
    /*@param


     */
    public int howManyTOC(TextView rhythm){}


    public void sendToSoundManager(int numberOfBPM){}

}
