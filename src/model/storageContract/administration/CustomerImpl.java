package model.storageContract.administration;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author Dennis Dominik Lehmann
 */
public class CustomerImpl implements Customer, Serializable {

    private String name;

    private BigDecimal maxValue;

    private Duration maxDurationOfStorage;

    public CustomerImpl() {
    }

    public CustomerImpl(String name) throws CustomerException {
        if (name == null) {
            throw new CustomerException("Es wurde null übergebn das ist nicht erlaubt.");
        }
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BigDecimal getMaxValue() {
        return maxValue;
    }

    @Override
    public Duration getMaxDurationOfStorage() {
        return maxDurationOfStorage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxDurationOfStorage(Duration duration) {
        this.maxDurationOfStorage = duration;
    }

    public void setMaxValue(BigDecimal value) {
        this.maxValue = value;
    }

    public String toString() {
        return this.name;
    }

}
