package control;

import model.businessLogice.BusinessLogic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControlModelTest {

    @Test
    public void creatControlModel(){
        ControlModel controlModel = new ControlModel(new BusinessLogic(5));
        assertEquals(ControlModel.class, controlModel.getClass());
    }

    @Test
    public void getModel_good(){
        ControlModel controlModel = new ControlModel(new BusinessLogic(5));
        assertEquals(5, controlModel.getModel().getAlleCargo().length);
    }

    @Test
    public void setModel_good(){
        ControlModel controlModel = new ControlModel(new BusinessLogic(5));
        controlModel.setModel(new BusinessLogic(7));
        assertEquals(7, controlModel.getModel().getAlleCargo().length);
    }
}