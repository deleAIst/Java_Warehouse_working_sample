package routing.event;

import java.util.EventObject;

/**
 * @author Dennis Dominik Lehmann
 */
public class HazardDisplayEvent extends EventObject {
    private final boolean included;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public HazardDisplayEvent(Object source, boolean included) {
        super(source);
        this.included=included;
    }

    public boolean isIncluded() {
        return included;
    }
}
