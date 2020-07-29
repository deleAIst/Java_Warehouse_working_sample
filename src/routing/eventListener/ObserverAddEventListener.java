package routing.eventListener;

import control.ControlModel;
import routing.EventListener;
import routing.event.ObserverAddEvent;
import view.CargoDeletObserver;
import view.HazardObserver;
import view.StorageObserver;
import view.StorageSpaceObserver;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class ObserverAddEventListener implements EventListener<ObserverAddEvent> {

    private final ControlModel model;

    public ObserverAddEventListener(ControlModel businessLogic) {
        this.model = businessLogic;
    }

    @Override
    public void onEvent(ObserverAddEvent event) {
        switch (event.getObserverClass()) {
            case "CargoDelet":
                model.getModel().login(new CargoDeletObserver(model.getModel()));
                break;
            case "Hazard":
                model.getModel().login(new HazardObserver(model.getModel()));
                break;
            case "Storage":
                model.getModel().login(new StorageObserver(model.getModel()));
                break;
            case "StorageSpace":
                model.getModel().login(new StorageSpaceObserver(model.getModel()));
                break;
        }
    }
}
