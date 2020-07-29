package routing.event;

/**
 * @author Dennis Dominik Lehmann
 */
public class CargoPersistentEvent extends java.util.EventObject {


    private final Boolean saveOrLoad;
    private final int place;

    public CargoPersistentEvent(Object source, Boolean saveOrLoad, int place) {
        super(source);
        this.saveOrLoad=saveOrLoad;
        this.place=place;
    }

    public Boolean getSaveOrLoad() {
        return saveOrLoad;
    }

    public int getPlace() {
        return place;
    }
}
