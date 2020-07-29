package routing.event;

import model.storageContract.cargo.Hazard;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
/**
 * @author Dennis Dominik Lehmann
 */
public class CargoLiquidInsertEvent extends java.util.EventObject {
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
    public CargoLiquidInsertEvent(Object source, String owner, BigDecimal value, Duration durationOfStorage, Collection<Hazard> hazard, boolean pressurized) {
        super(source);
        this.owner = owner;
        this.value = value;
        this.durationOfStorage = durationOfStorage;
        this.hazard = hazard;
        this.pressurized = pressurized;
    }

    public String getOwner() {
        return owner;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Collection<Hazard> getHazard() {
        return hazard;
    }

    public Duration getDurationOfStorage() {
        return durationOfStorage;
    }

    public Boolean getPressurized() {
        return pressurized;
    }
}