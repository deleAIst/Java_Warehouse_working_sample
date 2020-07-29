package routing.eventHandler;


import model.businessLogice.BusinessLogiceException;
import routing.EventListener;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Dennis Dominik Lehmann
 */
public class EventHandler<T> {

   private final List<EventListener> list=new LinkedList<>();


    public void add(EventListener listener ){
        list.add(listener);
    }

    public void remove(EventListener listener){
        list.remove(listener);
    }

    public void handle(T event) throws BusinessLogiceException, CustomerException, CargoException {
        for(EventListener listener: this.list){
            listener.onEvent(event);
        }
    }
}
