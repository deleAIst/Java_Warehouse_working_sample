package routing.eventListener;

import control.ControlModel;
import routing.EventListener;
import routing.event.PersistentEvent;
import serialize.JOSandJBP;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class PersistendEventListener implements EventListener<PersistentEvent> {
    private final ControlModel controlModel;

    public PersistendEventListener(ControlModel controlModel){
        this.controlModel=controlModel;
    }

    @Override
    public void onEvent(PersistentEvent event) {
        switch (event.getPersistensType()){
            case "saveJOS":
                JOSandJBP.serializeJOS(event.getFilename(), this.controlModel.getModel());
                break;
            case "saveJBP":
                JOSandJBP.serializeJBP(event.getFilename(), this.controlModel.getModel());
                break;
            case "loadJOS":
                if(JOSandJBP.deserializeJOS(event.getFilename())!=null) {
                    controlModel.setModel(JOSandJBP.deserializeJOS(event.getFilename()));
                }else {
                    throw new IllegalArgumentException("Es ist ein Fehler aufgetreten.");
                }
                break;
            case "loadJBP":
                if(JOSandJBP.deserializeJBP(event.getFilename())!=null) {
                    controlModel.setModel(JOSandJBP.deserializeJBP(event.getFilename()));
                }else {
                    throw new IllegalArgumentException("Es ist ein Fehler aufgetreten.");
                }
                break;
        }
    }
}
