package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import model.storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import model.storageContract.cargo.UnitisedCargoImpl;
import routing.EventListener;
import routing.event.CargoTypeDisplayEvent;
import view.ViewCLI;

import java.util.List;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CargoTypeDisplayEventListener implements EventListener<CargoTypeDisplayEvent> {

    private final ControlModel businessLogic;
    private final ViewCLI view;

    public CargoTypeDisplayEventListener(ControlModel businessLogic, ViewCLI view) {
        this.businessLogic = businessLogic;
        this.view=view;
    }

    public void onEvent(CargoTypeDisplayEvent event) throws BusinessLogiceException {
        if ("".equals(event.getType())) {
            Cargo[] cargoArray = businessLogic.getModel().getAlleCargo();
            for (int i = 0; i < cargoArray.length; i++) {
                if (cargoArray[i] != null) {
                    this.view.typeDisplay("Cargo auf Lagerposition: " + i);
                    this.view.typeDisplay("Das Datum der letzten Inspektion ist:" + cargoArray[i].getLastInspectionDate());
                }
            }
        } else {
            List<Cargo> cargoList = businessLogic.getModel().filterCargoType(event.getType());
            this.view.typeDisplay("Die Auflistung der Im Lager befindlichen Carogs des Typs: " + event.getType());
            for (Cargo cargo : cargoList) {
                if (cargo instanceof LiquidBulkCargoImpl) {
                    LiquidBulkCargoImpl liquidBulkCargo = (LiquidBulkCargoImpl) cargo;
                    this.view.typeDisplay("Cargo auf Lagerposition: " + liquidBulkCargo.getStorageSize());
                    this.view.typeDisplay("Das Datum der Einlagerung ist:" + liquidBulkCargo.getStorageDate());
                    this.view.typeDisplay("Das Datum der letzten Inspektion ist:" + liquidBulkCargo.getLastInspectionDate());

                } else if (cargo instanceof UnitisedCargoImpl) {
                    UnitisedCargoImpl unitisedCargo = (UnitisedCargoImpl) cargo;
                    this.view.typeDisplay("Cargo auf Lagerposition: " + unitisedCargo.getStorageSize());
                    this.view.typeDisplay("Das Datum der Einlagerung ist:" + unitisedCargo.getStorageDate());
                    this.view.typeDisplay("Das Datum der letzten Inspektion ist:" + unitisedCargo.getLastInspectionDate());

                } else if (cargo instanceof MixedCargoLiquidBulkAndUnitisedImpl) {
                    MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised = (MixedCargoLiquidBulkAndUnitisedImpl) cargo;
                    this.view.typeDisplay("Cargo auf Lagerposition: " + mixedCargoLiquidBulkAndUnitised.getStorageSize());
                    this.view.typeDisplay("Das Datum der Einlagerung ist:" + mixedCargoLiquidBulkAndUnitised.getStorageDate());
                    this.view.typeDisplay("Das Datum der letzten Inspektion ist:" + mixedCargoLiquidBulkAndUnitised.getLastInspectionDate());
                }
                this.view.typeDisplay("");
            }
        }
    }
}
