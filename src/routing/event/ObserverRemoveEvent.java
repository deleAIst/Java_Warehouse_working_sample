package routing.event;

import java.util.EventObject;

/**
 * @author Dennis Dominik Lehmann
 */
public class ObserverRemoveEvent extends EventObject {
    private final String observerClass;

    public ObserverRemoveEvent(Object source, String observerClass) {
        super(source);
        this.observerClass = observerClass;
    }

    public String getObserverClass() {
        return observerClass;
    }
}
