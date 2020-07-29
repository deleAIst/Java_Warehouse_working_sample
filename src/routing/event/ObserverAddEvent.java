package routing.event;

import java.util.EventObject;

/**
 * @author Dennis Dominik Lehmann
 */
public class ObserverAddEvent extends EventObject {

    private final String observerClass;

    public ObserverAddEvent(Object source, String observerClass) {
        super(source);
        this.observerClass = observerClass;
    }

    public String getObserverClass() {
        return observerClass;
    }
}
