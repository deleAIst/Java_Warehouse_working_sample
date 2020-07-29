package routing.eventListener;

import routing.EventListener;
import routing.event.HazardOutputEvent;
import view.ViewCLI;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class HazardOutputEventListener implements EventListener<HazardOutputEvent> {

    private final ViewCLI view;

    public HazardOutputEventListener(ViewCLI view){
        this.view=view;
    }

    @Override
    public void onEvent(HazardOutputEvent event) {
        if (event.getFlage()){
            view.hazardInclude(event.getHazards());
        }else {
            view.hazardNotInclude(event.getHazards());
        }
    }
}
