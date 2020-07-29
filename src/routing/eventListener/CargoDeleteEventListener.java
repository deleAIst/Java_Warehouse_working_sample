package routing.eventListener;

import control.ControlModel;
import model.storageContract.cargo.CargoException;
import routing.EventListener;
import routing.event.CargoDeleteEvent;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CargoDeleteEventListener implements EventListener<CargoDeleteEvent> {

    private final ControlModel model;

    public CargoDeleteEventListener(ControlModel model){
        this.model=model;
    }

    public void onEvent(CargoDeleteEvent event) throws CargoException {
       boolean f=this.model.getModel().deletCargo(event.getStorageLocation());
       if(!f){
          throw new CargoException("Es ist kein Cargo an disem Platz um es zu Löschen oder sie sind Out of Rang von der größe des Lagerns");
       }
    }
}
