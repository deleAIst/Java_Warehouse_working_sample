package routing.event;

import java.util.EventObject;

/**
 * @author Dennis Dominik Lehmann
 */
public class CargoTypeDisplayEvent extends EventObject {
    private final String type;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CargoTypeDisplayEvent(Object source, String type) {
        super(source);
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
