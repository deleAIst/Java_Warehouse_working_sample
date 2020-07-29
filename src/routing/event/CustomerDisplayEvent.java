package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class CustomerDisplayEvent extends java.util.EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CustomerDisplayEvent(Object source) {
        super(source);
    }

}
