package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private final SalesSystemDAO dao;
    private static final Logger log = LogManager.getLogger(StockController.class);
    @FXML
    private Button addItem;
    @FXML
    private TableView<StockItem> warehouseTableView;
    private javafx.scene.control.TableColumn<StockItem, Long> idColumn = new TableColumn<>("Id");
    private javafx.scene.control.TableColumn<StockItem, String> nameColumn = new TableColumn<>("Name");
    private javafx.scene.control.TableColumn<StockItem, String> descriptionColumn = new TableColumn<>("Description");
    private javafx.scene.control.TableColumn<StockItem, Double> priceColumn = new TableColumn<>("Price");
    private javafx.scene.control.TableColumn<StockItem, Integer> quantityColumn = new TableColumn<>("Quantity");
    private java.awt.TextField barCodeField;
    private javafx.scene.control.TextField amountField;
    private javafx.scene.control.TextField descriptionField;
    private javafx.scene.control.TextField nameField;
    private javafx.scene.control.TextField priceField;
    private Button refreshWarehousebutton;
    private Button addProductbutton;


    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
          refreshStockItems();
        // TODO refresh view after adding new items
    }

    @FXML
    public void refreshButtonClicked()
    {
        log.info("Refresh button clicked");
        refreshStockItems();
    }

    private void refreshStockItems() {
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
        warehouseTableView.setItems(new ObservableListWrapper<>(dao.findStockItems()));
        warehouseTableView.getColumns().addAll(idColumn,nameColumn,descriptionColumn,priceColumn,quantityColumn);
        warehouseTableView.refresh();

    }
    @FXML
    protected void addButtonClicked() {
        log.info("Add button clicked");
        //size
        if(barCodeField != null && amountField != null && descriptionField != null && nameField != null && priceField != null ){
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
        }
    }

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

}
