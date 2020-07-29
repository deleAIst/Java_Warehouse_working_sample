package serialize;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.cargo.Cargo;

import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author Dennis Dominik Lehmann
 */
public class JOSandJBP {

    public static void serializeJOS(String filename, BusinessLogic businessLogic) {
        if (filename == null || businessLogic == null) {
            throw new IllegalArgumentException();
        }
        if(new File(filename).exists()){
            throw new IllegalArgumentException("Das File Existiert schon.");
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename.concat(".txt")));
            oos.writeObject(businessLogic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(111);
        }
    }

    public static BusinessLogic deserializeJOS(String filename) {
        if (filename == null || !new File(filename.concat(".txt")).exists()) {
            throw new IllegalArgumentException();
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename.concat(".txt")));
            return (BusinessLogic) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }

    public static void serializeJBP(String filename, BusinessLogic businessLogic) {
        if (filename == null || businessLogic == null) {
            throw new IllegalArgumentException();
        }
        if(new File(filename).exists()){
            throw new IllegalArgumentException("Das File Existiert schon.");
        }
        try (XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename.concat(".xml"))))) {
            PersistenceDelegate pd = encoder.getPersistenceDelegate(Integer.class);
            encoder.setPersistenceDelegate(BigDecimal.class, pd);
            PersistenceDelegate pd2 = encoder.getPersistenceDelegate(String.class);
            encoder.setPersistenceDelegate(Duration.class, pd2);
            encoder.writeObject(businessLogic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BusinessLogic deserializeJBP(String filename) {
        if (filename == null || !new File(filename.concat(".xml")).exists()) {
            throw new IllegalArgumentException();
        }
        try (XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename.concat(".xml"))))) {
            return (BusinessLogic) decoder.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void serializeCargo(int place, BusinessLogic businessLogic) throws BusinessLogiceException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("CargoSave.txt"));
            Cargo cargo = businessLogic.getCargo(place);
            if (cargo == null) {
                throw new BusinessLogiceException("Es beindet sich kein Cargo an dem Platz.");
            } else {
                oos.writeObject(cargo);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.exit(111);
        }
    }

    public static Cargo deserializeCargo() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("CargoSave.txt"));
            return (Cargo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return null;
    }
}

