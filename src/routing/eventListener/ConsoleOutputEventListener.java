package routing.eventListener;

import routing.EventListener;
import routing.event.ConsoleOutputEvent;
import view.ViewCLI;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class ConsoleOutputEventListener implements EventListener<ConsoleOutputEvent> {

    private final ViewCLI view;

    public ConsoleOutputEventListener(ViewCLI view){
        this.view=view;
    }

    public void onEvent(ConsoleOutputEvent event ){
        if(event.getError()){
            view.printError(event.getTrigger());
        }else{
            view.cliInstructionsAndNotes(event.getTrigger());
        }

    }
}
