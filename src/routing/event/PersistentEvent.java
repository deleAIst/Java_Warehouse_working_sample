package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class PersistentEvent  extends java.util.EventObject{


    private final String persistensType;
    private final String filename;

    public PersistentEvent(Object source, String persistensType, String filename) {
        super(source);
        this.persistensType=persistensType;
        this.filename=filename;
    }

    public String getFilename() {
        return filename;
    }

    public String getPersistensType() {
        return persistensType;
    }
}
