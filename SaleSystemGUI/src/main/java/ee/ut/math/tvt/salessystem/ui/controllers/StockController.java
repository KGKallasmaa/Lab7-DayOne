package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private final SalesSystemDAO dao;
    private static final Logger log = LogManager.getLogger(StockController.class);
    @FXML private Button addItem;
    @FXML private Button removeproduct;
    @FXML private TableView<StockItem> warehouseTableView;
    @FXML private javafx.scene.control.TableColumn<StockItem, Long> idColumn = new TableColumn<>("Id");
    @FXML private javafx.scene.control.TableColumn<StockItem, String> nameColumn = new TableColumn<>("Name");
    @FXML private javafx.scene.control.TableColumn<StockItem, String> descriptionColumn = new TableColumn<>("Description");
    @FXML private javafx.scene.control.TableColumn<StockItem, Double> priceColumn = new TableColumn<>("Price");
    @FXML private javafx.scene.control.TableColumn<StockItem, Integer> quantityColumn = new TableColumn<>("Quantity");
    @FXML private javafx.scene.control.TextField barCodeField;
    @FXML private javafx.scene.control.TextField nameField;
    @FXML private javafx.scene.control.TextField descriptionField;
    @FXML private javafx.scene.control.TextField priceField;
    @FXML private javafx.scene.control.TextField amountField;
    @FXML private Button refreshWarehousebutton;
    @FXML private Button addProductbutton;
    @FXML private Button removeproductButton;

    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(120);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(120);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(120);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(120);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setPrefWidth(120);
        warehouseTableView.getColumns().addAll(idColumn,nameColumn,descriptionColumn,priceColumn,quantityColumn);
        refreshStockItems();

        /* // lambda for remove product, not integrated
        removeproductButton.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                    if (!newPropertyValue) {

                    }
                }
        });*/
    }

    @FXML public void refreshButtonClicked() {
        log.info("Refresh button clicked");
        refreshStockItems();
    }
    @FXML protected void addButtonClicked() {
        log.info("Add button clicked");
        //System.out.println(barCodeField.getText());
        //textfields are always equal to null
        if(!barCodeField.getText().isEmpty() && !nameField.getText().isEmpty() && !descriptionField.getText().isEmpty() && !priceField.getText().isEmpty() && !amountField.getText().isEmpty()){
            StockItem new_stockitem = new StockItem(Long.parseLong(barCodeField.getText()),nameField.getText(),descriptionField.getText(),
                    Double.parseDouble(priceField.getText()),Integer.parseInt(amountField.getText()));
            int before_length = dao.findStockItems().size();
            dao.saveStockItem(new_stockitem);
            int after_length = dao.findStockItems().size();
            if(after_length > before_length){
                log.info("Item saved");
            }else{
                log.info("Item was not saved");
            }
        } else {
            log.info("Found a field that was equal to null.");
        }
    }
    @FXML public void removeButtonClicked(){
        log.info("Remove button clicked");
        if(!barCodeField.getText().isEmpty() && !nameField.getText().isEmpty() && !descriptionField.getText().isEmpty() && !priceField.getText().isEmpty() && !amountField.getText().isEmpty()){
            StockItem old_stockitem = new StockItem(Long.parseLong(barCodeField.getText()),nameField.getText(),descriptionField.getText(),
                    Double.parseDouble(priceField.getText()),Integer.parseInt(amountField.getText()));
            int before_length = dao.findStockItems().size();
            dao.removeStockItem(old_stockitem);
            int after_length = dao.findStockItems().size();
            if(after_length < before_length){
                log.info("Item removed");
            }else{
                log.info("Item was not removed");
            }
        } else {
            log.info("Found a field that was equal to null.");
        }
    }

    private void refreshStockItems() {
        warehouseTableView.setItems(new ObservableListWrapper<>(dao.findStockItems()));
        warehouseTableView.refresh();
    }
/*
    @FXML protected void addAmount(){
        log.info("Amount selected");
        this.amountField = amountField;
    }
    @FXML protected void addBarcode(){
        log.info("Barcode selected");
        this.barCodeField = barCodeField;
    }
    @FXML protected void addName(){
        log.info("Name selected");
        this.nameField = nameField;
    }
    @FXML protected void addDesc(){
        log.info("Description selected");
        this.descriptionField = descriptionField;
    }
    @FXML protected void addPrice(){
        log.info("Price selected");
        this.priceField = priceField;
    }
*/
/*
    private int getQuantity () { return Integer.parseInt(amountField.getText());}
    private long getId () { return  Long.parseLong(barCodeField.getText());}
    private String getName () { return nameField.getText();}
    private double getPrice () { return Double.parseDouble(priceField.getText());}
    private String getDescription () { return descriptionField.getText();}
*/
}
