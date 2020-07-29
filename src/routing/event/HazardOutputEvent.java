package routing.event;

import model.storageContract.cargo.Hazard;

import java.util.EventObject;
import java.util.HashSet;

/**
 * @author Dennis Dominik Lehmann
 */
public class HazardOutputEvent extends EventObject {

    private final HashSet<Hazard> hazards;
    private final Boolean flage;

    public HazardOutputEvent(Object source, HashSet<Hazard> hazards, boolean flage) {
        super(source);
        this.hazards=hazards;
        this.flage=flage;
    }

    public HashSet<Hazard> getHazards() {
        return hazards;
    }

    public Boolean getFlage() {
        return flage;
    }
}
