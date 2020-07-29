package view;

import model.businessLogice.BusinessLogic;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


/**
 * @author Dennis Dominik Lehmann
 */
class StorageSpaceObserverTest {

    @Test
    public void storageSpaceObserverIntalization() {
        BusinessLogic businessLogic = new BusinessLogic(1);
        StorageSpaceObserver observer = new StorageSpaceObserver(businessLogic);
        assertEquals(StorageSpaceObserver.class, observer.getClass());
    }

    //Inspieriert von http://www.adam-bien.com/roller/abien/entry/testing_system_out_println_outputs
    @Test
    public void update_PrintByOnSize(){
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);
        BusinessLogic businessLogic = new BusinessLogic(1);
        Observer observer =new StorageSpaceObserver(businessLogic);
        observer.update();
        verify(out).println("Nur noch ein Platz im Lager.");
    }

    @Test
    public void update_NotPrintByOverOnSize(){
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);
        BusinessLogic businessLogic = new BusinessLogic(2);
        Observer observer =new StorageSpaceObserver(businessLogic);
        observer.update();
        verify(out, times(0)).println("Nur noch ein Platz im Lager.");
    }

    @Test
    public void update_NotPrintByUnderOnSize(){
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);
        BusinessLogic businessLogic = new BusinessLogic(0);
        Observer observer =new StorageSpaceObserver(businessLogic);
        observer.update();
        verify(out, times(0)).println("Nur noch ein Platz im Lager.");
    }

}