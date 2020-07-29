package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import routing.EventListener;
import routing.event.HazardDisplayEvent;
import routing.event.HazardOutputEvent;
import routing.eventHandler.EventHandler;

import java.util.HashSet;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class HazardDisplayEventListener  implements EventListener<HazardDisplayEvent> {

    private final ControlModel businessLogic;
    private final EventHandler<HazardOutputEvent> handler;

    public HazardDisplayEventListener(ControlModel businessLogic, EventHandler<HazardOutputEvent> handler){
        this.businessLogic=businessLogic;
       this.handler=handler;
    }

    public void onEvent(HazardDisplayEvent event) throws BusinessLogiceException, CustomerException, CargoException {
        HashSet<Hazard> hazardList = new HashSet<>();
        HashSet<Hazard> hazardAll = new HashSet<>();
        hazardAll.add(Hazard.toxic);
        hazardAll.add(Hazard.explosive);
        hazardAll.add(Hazard.flammable);
        hazardAll.add(Hazard.radioactive);
        Cargo[] cargos = businessLogic.getModel().getAlleCargo();
        if(event.isIncluded()){
            for (Cargo cargo : cargos) {
                if (cargo != null) {
                    hazardList.addAll(cargo.getHazards());
                }
            }
            HazardOutputEvent hazardsIncluded = new HazardOutputEvent(this, hazardList, true );
            handler.handle(hazardsIncluded);
        }else {
            for (Cargo cargo : cargos) {
                if (cargo != null) {
                    hazardAll.removeAll(cargo.getHazards());
                }
            }
            HazardOutputEvent hazardsIncluded = new HazardOutputEvent(this, hazardAll, false );
            handler.handle(hazardsIncluded);
        }
    }
}
