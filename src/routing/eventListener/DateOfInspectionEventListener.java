package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import routing.EventListener;
import routing.event.DateOfInspectionEvent;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class DateOfInspectionEventListener implements EventListener<DateOfInspectionEvent> {

    private final ControlModel model;

    public DateOfInspectionEventListener(ControlModel model){
        this.model=model;
    }

    public void onEvent(DateOfInspectionEvent event) throws BusinessLogiceException {
        this.model.getModel().updateInspectionDate(event.getStorageLocation());
    }
}
