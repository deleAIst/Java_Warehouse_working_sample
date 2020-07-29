package model.storageContract.cargo;

import model.storageContract.administration.Customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class LiquidBulkCargoImpl implements LiquidBulkCargo, Serializable {
    private boolean pressurized;
    private Date inspectionDate;
    private int storageSize;
    private Date storageDate;
    private Customer owner;
    private Collection<Hazard> hazards;
    private Duration durationOfStorage;
    private BigDecimal value;

    public LiquidBulkCargoImpl() {
    }

    public LiquidBulkCargoImpl(Customer owner, BigDecimal value, Duration durationOfStorage, Collection<Hazard> hazard, boolean pressurized) {
        this.owner = owner;
        this.value = value;
        this.durationOfStorage = durationOfStorage;
        this.hazards = hazard;
        this.pressurized = pressurized;
    }

    @Override
    public boolean isPressurized() {
        return this.pressurized;
    }

    public void setPressurized(boolean pressurized) { this.pressurized = pressurized; }

    @Override
    public Customer getOwner() {
        return this.owner;
    }

    public void setOwner(Customer owner) { this.owner = owner; }

    @Override
    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) { this.value = value; }

    @Override
    public Duration getDurationOfStorage() {
        return this.durationOfStorage;
    }

    public void setDurationOfStorage(Duration durationOfStorage) { this.durationOfStorage = durationOfStorage; }

    @Override
    public Collection<Hazard> getHazards() {
        return this.hazards;
    }

    public void setHazards(Collection<Hazard> hazard) {
        this.hazards = hazard;
    }

    @Override
    public Date getLastInspectionDate() {
        return this.inspectionDate;
    }

    public void setLastInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public int getStorageSize() { return this.storageSize; }

    public void setStorageSize(int storageSize) {
        this.storageSize = storageSize;
    }

    public Date getStorageDate() { return storageDate; }

    public void setStorageDate(Date storageDate) {
        this.storageDate = storageDate;
    }





}
