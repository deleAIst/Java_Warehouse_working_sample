package control;

import model.businessLogice.BusinessLogic;

import java.io.Serializable;

/**
 * @author Dennis Dominik Lehmann
 */
public class ControlModel implements Serializable {
    BusinessLogic model;

   public ControlModel(BusinessLogic model){
       this.model=model;
   }

    public BusinessLogic getModel() {
        return model;
    }

    public void setModel(BusinessLogic model) {
        this.model = model;
    }
}
