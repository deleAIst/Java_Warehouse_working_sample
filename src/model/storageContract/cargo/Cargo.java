package model.storageContract.cargo;

import model.storageContract.administration.Customer;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;

public interface Cargo extends Serializable {
    Customer getOwner();
    BigDecimal getValue();
    Duration getDurationOfStorage();
    Collection<Hazard> getHazards();
    Date getLastInspectionDate();
}
