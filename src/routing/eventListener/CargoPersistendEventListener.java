package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.cargo.Cargo;
import routing.EventListener;
import routing.event.CargoPersistentEvent;
import serialize.JOSandJBP;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CargoPersistendEventListener implements EventListener<CargoPersistentEvent> {

    private final ControlModel model;

    public CargoPersistendEventListener(ControlModel model) {
        this.model = model;
    }

    @Override
    public void onEvent(CargoPersistentEvent event) throws BusinessLogiceException {

        if (event.getSaveOrLoad()) {
            JOSandJBP.serializeCargo(event.getPlace(), model.getModel());
        } else {
            Cargo cargo = JOSandJBP.deserializeCargo();
            Cargo[] cargos = model.getModel().getAlleCargo();
            if(event.getPlace()<cargos.length) {
                if (cargos[event.getPlace()] == null) {
                    cargos[event.getPlace()] = cargo;
                    model.getModel().setCargoStorage(cargos);
                } else {
                    throw new IllegalArgumentException("An dem Platz befindet sich schon ein Cargo");
                }
            }else {
                throw new IllegalArgumentException("Die Size ist zu groß für das Lager.");
            }
        }
    }
}
