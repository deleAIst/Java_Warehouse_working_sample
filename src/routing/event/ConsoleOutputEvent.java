package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class ConsoleOutputEvent extends java.util.EventObject{

    private final String trigger;
    private final Boolean error;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ConsoleOutputEvent(Object source, String trigger, Boolean error) {
        super(source);
        this.trigger=trigger;
        this.error= error;
    }

    public String getTrigger() {
        return trigger;
    }

    public Boolean getError() {
        return error;
    }
}
