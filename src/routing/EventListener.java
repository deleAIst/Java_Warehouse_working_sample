package routing;

import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;

/**
 * @author Dennis Dominik Lehmann
 */
public interface EventListener<T> {

    void onEvent(T event) throws BusinessLogiceException, CustomerException, CargoException;
}
