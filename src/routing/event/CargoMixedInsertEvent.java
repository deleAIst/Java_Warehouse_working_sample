package routing.event;

import model.storageContract.cargo.Hazard;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;

/**
 * @author Dennis Dominik Lehmann
 */
public class CargoMixedInsertEvent extends java.util.EventObject {
    private final boolean fragile;
    private final String owner;
    private final boolean pressurized;
    private final Collection<Hazard> hazard;
    private final Duration durationOfStorage;
    private final BigDecimal value;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public CargoMixedInsertEvent(Object source, String owner, BigDecimal value, Duration durationOfStorage, Collection<Hazard> hazard, boolean pressurized, boolean fragile) {
        super(source);
        this.owner = owner;
        this.value = value;
        this.durationOfStorage = durationOfStorage;
        this.hazard = hazard;
        this.pressurized = pressurized;
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

    public Boolean getPressurized() {
        return this.pressurized;
    }

    public boolean getFragile(){
        return this.fragile;
    }
}
