package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class CargoDeleteEvent extends java.util.EventObject {
    private final int storageLocation;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CargoDeleteEvent(Object source, int storageLocation) {
        super(source);
        this.storageLocation=storageLocation;
    }

    public int getStorageLocation() {
        return storageLocation;
    }
}
