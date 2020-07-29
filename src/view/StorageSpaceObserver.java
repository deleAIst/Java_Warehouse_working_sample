package view;

import model.businessLogice.BusinessLogic;
import model.storageContract.cargo.Cargo;

import java.io.Serializable;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class StorageSpaceObserver implements Observer, Serializable {

    private final BusinessLogic businessLogic;

    public StorageSpaceObserver(BusinessLogic businessLogic){
        this.businessLogic = businessLogic;
    }

    @Override
    public void update() {
        int counter = 0;
        Cargo[] ca = this.businessLogic.getAlleCargo();
        for (Cargo i : ca) {
            if (i == null) {
                counter += 1;
            }
        }
        if (counter == 1) {
            System.out.println("Nur noch ein Platz im Lager.");
        }
    }
}
