package routing.event;

import model.storageContract.cargo.Hazard;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

/**
 * @author Dennis Dominik Lehmann
 */
public class CargoUnitisedInsertEvent extends java.util.EventObject {
    private final String owner;
    private final BigDecimal value;
    private final Duration durationOfStorage;
    private final Collection<Hazard> hazard;
    private final boolean fragile;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CargoUnitisedInsertEvent(Object source, String owner, BigDecimal value, Duration durationOfStorage, Collection<Hazard> hazard, boolean fragile) {
        super(source);
        this.owner = owner;
        this.value = value;
        this.durationOfStorage = durationOfStorage;
        this.hazard = hazard;
        this.fragile=fragile;
    }

    public String getOwner() {
        return this.owner;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Collection<Hazard> getHazard() {
        return this.hazard;
    }

    public Duration getDurationOfStorage() {
        return this.durationOfStorage;
    }

    public boolean getFragile(){
        return this.fragile;
    }
}
