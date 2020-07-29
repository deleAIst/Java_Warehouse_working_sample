package view;

import model.businessLogice.BusinessLogic;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.Hazard;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class HazardObserver implements Observer , Serializable {
    private final BusinessLogic businessLogic;
    private final HashSet<Hazard> hazards = new HashSet<>();


    public HazardObserver(BusinessLogic businessLogic) {
        this.businessLogic = businessLogic;
    }

    @Override
    public void update() {
        int oldSize= hazards.size();
        hazards.clear();
        Cargo[] alleCargos = businessLogic.getAlleCargo();
        for (Cargo cargo : alleCargos) {
            if (cargo != null) {
                hazards.addAll(cargo.getHazards());
            }
        }
        int newSize= hazards.size();
        if (newSize != oldSize) {
            System.out.println("Es gabe eine änderung an den Gefahrenstoffen im Lager.");
            System.out.println("Die Neuen gefaren Stoffe im Lager sind: ");
            System.out.println(hazards);
        }
    }
}
