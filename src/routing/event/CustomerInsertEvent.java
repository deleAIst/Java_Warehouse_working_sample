package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class CustomerInsertEvent extends java.util.EventObject {
    private final String name;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CustomerInsertEvent(Object source, String name) {
        super(source);
        this.name=name;
    }

    public String getName() {
        if(this.name.equals("")){
            return null;
        }
        return name;
    }
}
