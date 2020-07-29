package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class CustomerDeleteEvent extends java.util.EventObject {
    private final String name;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CustomerDeleteEvent(Object source, String name) {
        super(source);
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
