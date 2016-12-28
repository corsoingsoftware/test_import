package a2016.soft.ing.unipd.metronomepro.adapters.listeners;

import a2016.soft.ing.unipd.metronomepro.entities.ParcelableSong;

/**
 * Created by giuli on 28/12/2016.
 */
//ho usato il parametro int position per sapere quale elemento della lista sto selezionando!
public interface OnItemClickListener {
    void OnItemClick(ParcelableSong item,int position);
}
