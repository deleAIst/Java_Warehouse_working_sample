package view;

import model.businessLogice.BusinessLogic;
import model.storageContract.cargo.Cargo;

import java.io.Serializable;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class StorageObserver implements Observer, Serializable {

    private final BusinessLogic businessLogic;
    private int count =0;

    public StorageObserver(BusinessLogic businessLogic){
        this.businessLogic = businessLogic;
    }

    @Override
    public void update() {
        Cargo[] storage = businessLogic.getAlleCargo();
        int countNew=0;
        for (Cargo c: storage){
            if(c!=null){
                countNew++;
            }
        }
        if (countNew>count){
            int freeStorage =storage.length-countNew;
            System.out.println("Es wurde ein Cargo eingelagert. In Lager:" + businessLogic.hashCode() + " hat noch Lagerfläche von:" + freeStorage);
            count=countNew;
        }
    }
}
