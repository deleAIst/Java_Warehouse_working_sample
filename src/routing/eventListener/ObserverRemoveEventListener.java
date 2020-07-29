package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import routing.EventListener;
import routing.event.ObserverRemoveEvent;
import view.Observer;

import java.util.List;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class ObserverRemoveEventListener implements EventListener<ObserverRemoveEvent> {

    private final ControlModel model;

    public ObserverRemoveEventListener(ControlModel model) {
        this.model = model;
    }

    @Override
    public void onEvent(ObserverRemoveEvent event) throws BusinessLogiceException{
        List<Observer> observerList = model.getModel().getObserverList();
        switch (event.getObserverClass()) {
            case "CargoDelet":
                model.getModel().logOff(this.getObserverObject("CargoDelet", observerList));
                break;
            case "Hazard":
                model.getModel().logOff(this.getObserverObject("Hazard", observerList));
                break;
            case "Storage":
                model.getModel().logOff(this.getObserverObject("Storage", observerList));
                break;
            case "StorageSpace":
                model.getModel().logOff(this.getObserverObject("StorageSpace", observerList));
                break;
        }
    }
    private Observer getObserverObject(String search, List<Observer> observerList) throws BusinessLogiceException {
        for(Observer observer: observerList){
            if(search.equals("CargoDelet")){
                if("CargoDeletObserver".equals(observer.getClass().getSimpleName())){
                    return observer;
                }
            }else if(search.equals("Hazard")){
                if("HazardObserver".equals(observer.getClass().getSimpleName())){
                    return observer;
                }
            }else if(search.equals("Storage")){
                if("StorageObserver".equals(observer.getClass().getSimpleName())){
                    return observer;
                }
            }else if(search.equals("StorageSpace")){
                if("StorageSpaceObserver".equals(observer.getClass().getSimpleName())){
                    return observer;
                }
            }
        }
        throw  new BusinessLogiceException("Kein observer von disem Type vorhanden");
    }
}
