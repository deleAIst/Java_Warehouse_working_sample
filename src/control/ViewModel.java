package control;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;


/**
 * @author Dennis Dominik Lehmann
 */
public class ViewModel {
    private static final ObservableList<Cargo> data = FXCollections.observableArrayList();

    @FXML
    private TableView<Cargo> cargoTabell;
    @FXML
    private TableColumn<Cargo, String> cargoTypeColumn;
    @FXML
    private TableColumn<Cargo, String> ownerNameColumn;
    @FXML
    private TableColumn<Cargo, String> cargoValueColumn;
    @FXML
    private ChoiceBox cargoTypeChoiceBox;
    @FXML
    private TextField ownerTextField;
    @FXML
    private TextField valueTextField;
    @FXML
    private TextField durationOfStorageTextField;
    @FXML
    private CheckBox pressurizedChiceBox;
    @FXML
    private CheckBox fragileChiceBox;
    @FXML
    private Slider deleteSizeChoiceBox;
    @FXML
    private CheckBox explosiveCheckBox;
    @FXML
    private CheckBox toxicCheckBox;
    @FXML
    private CheckBox flammableCheckBox;
    @FXML
    private CheckBox radiacionCheckBox;

    private BusinessLogic model;

    private final StringProperty ownerNameProperty = new SimpleStringProperty();
    private final StringProperty valueProperty = new SimpleStringProperty();
    private final StringProperty durationOfStorageProperty = new SimpleStringProperty();
    private final StringProperty hazardsProperty = new SimpleStringProperty();
    private final StringProperty lastInspectionDateProperty = new SimpleStringProperty();
    private final ListProperty<Cargo> cargosTabelProperty = new SimpleListProperty<>();

    public StringProperty ownerNameProperty() {
        return ownerNameProperty;
    }

    public void setOwnerNameProperty(String name) {
        this.ownerNameProperty.set(name);
    }

    public String getOwnerNameProperty() {
        return this.ownerNameProperty.get();
    }

    public StringProperty valueProperty() {
        return this.valueProperty;
    }

    public void setValueProperty(String value) {
        this.valueProperty.set(value);
    }

    public String getValueProperty() {
        return this.valueProperty.get();
    }

    public StringProperty durationOfStorageProperty() {
        return durationOfStorageProperty;
    }

    public void setDurationOfStorageProperty(String duration) {
        this.durationOfStorageProperty.set(duration);
    }

    public String getDurationOfStorageProperty() {
        return this.durationOfStorageProperty.get();
    }

    public StringProperty hazardsProperty() {
        return hazardsProperty;
    }

    public void setHazardsProperty(String hazarde) {
        this.hazardsProperty.set(hazarde);
    }

    public String getHazardsProperty() {
        return this.hazardsProperty.get();
    }

    public StringProperty lastInspectionDateProperty() {
        return lastInspectionDateProperty;
    }

    public void setLastInspectionDateProperty(String date) {
        this.lastInspectionDateProperty.set(date);
    }

    public String getLastInspectionDateProperty() {
        return this.lastInspectionDateProperty.get();
    }

    public ListProperty cargosTabellProperty() {
        return cargosTabelProperty;
    }

    public void setCargosTabellProperty(ObservableList<Cargo> list) {
        this.cargosTabelProperty.set(list);
    }

    public ObservableList<Cargo> getCargosTabellProperty() {
        return this.cargosTabelProperty.get();
    }


    public void load(BusinessLogic businessLogic){
        this.model=businessLogic;
        this.updateProperties();
    }
    @FXML
    private void initialize() {
        this.cargoTabell.itemsProperty().bindBidirectional(cargosTabelProperty);
    }

    public void addButtonClicke(ActionEvent actionEvent) {
        try {
            switch (this.cargoTypeChoiceBox.getSelectionModel().getSelectedItem().toString()) {
                case "Liquid":
                    this.liquid();
                    break;
                case "Unitised":
                    this.unitised();
                    break;
                case "Mixed":
                    this.mixed();
                    break;
                default:
                    break;
            }
            this.valueTextField.setStyle("-fx-text-inner-color: black;");
            this.durationOfStorageTextField.setStyle("-fx-text-inner-color: black;");
        } catch (CargoException | BusinessLogiceException | CustomerException | NullPointerException | NumberFormatException ce) {
            this.valueTextField.setStyle("-fx-text-inner-color: red;");
            this.durationOfStorageTextField.setStyle("-fx-text-inner-color: red;");
            ce.getStackTrace();
        }

        updateProperties();


    }

    public void deleteButtonClicke(ActionEvent actionEvent) {
        model.deletCargo((int) deleteSizeChoiceBox.getValue());
        this.updateProperties();
    }


    private void updateProperties() {
        List<Cargo> list = new ArrayList<>();
        Cargo[] cargos = model.getAlleCargo();
        data.clear();
        for (int i = 0; i < cargos.length; i++) {
            if (cargos[i] != null) {
                list.add(cargos[i]);
            }
        }
        data.addAll(list);
        this.cargosTabelProperty.set(data);
    }

    private void liquid() throws CargoException, BusinessLogiceException, CustomerException, NumberFormatException {
        Customer customer = customerAvailable();
        Collection<Hazard> hazard = this.hazards();
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(Double.parseDouble(this.valueTextField.getText())), Duration.ofMinutes(Long.parseLong(this.durationOfStorageTextField.getText())), hazard, this.pressurizedChiceBox.isSelected());
        model.putCargo(cargo);
    }

    private void unitised() throws CargoException, BusinessLogiceException, CustomerException, NumberFormatException {
        Customer customer = customerAvailable();
        Collection<Hazard> hazard = this.hazards();
        Cargo cargo = new UnitisedCargoImpl(customer, BigDecimal.valueOf(Double.parseDouble(this.valueTextField.getText())), Duration.ofMinutes(Long.parseLong(this.durationOfStorageTextField.getText())), hazard, this.fragileChiceBox.isSelected());
        model.putCargo(cargo);
    }

    private void mixed() throws CargoException, BusinessLogiceException, CustomerException, NumberFormatException {
        Customer customer = customerAvailable();
        Collection<Hazard> hazard = this.hazards();
        Cargo cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.valueOf(Double.parseDouble(this.valueTextField.getText())), Duration.ofMinutes(Long.parseLong(this.durationOfStorageTextField.getText())), hazard, this.pressurizedChiceBox.isSelected(), this.fragileChiceBox.isSelected());
        model.putCargo(cargo);
    }

    private Customer customerAvailable() throws CustomerException {
        if (null == model.getCustomer(this.ownerTextField.getText())) {
            return model.putCustomer(this.ownerTextField.getText());
        } else {
            return model.getCustomer(this.ownerTextField.getText());
        }
    }

    private Collection<Hazard> hazards() {
        Collection<Hazard> hazards = new HashSet<>();
        if (this.toxicCheckBox.isSelected()) {
            hazards.add(Hazard.toxic);
        }
        if (this.flammableCheckBox.isSelected()) {
            hazards.add(Hazard.flammable);
        }
        if (this.explosiveCheckBox.isSelected()) {
            hazards.add(Hazard.explosive);
        }
        if (this.radiacionCheckBox.isSelected()) {
            hazards.add(Hazard.radioactive);
        }
        return hazards;
    }
}
