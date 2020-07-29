package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class DateOfInspectionEvent extends java.util.EventObject{
    private final int storageLocation;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DateOfInspectionEvent(Object source, int storageLocation) {
        super(source);
        this.storageLocation=storageLocation;
    }

    public int getStorageLocation() {
        return storageLocation;
    }
}
