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
    @FXML private Button addItem;
    @FXML private Button removeproduct;
    @FXML private TableView<StockItem> warehouseTableView;
    @FXML private javafx.scene.control.TableColumn<StockItem, Long> idColumn = new TableColumn<>("Id");
    @FXML private javafx.scene.control.TableColumn<StockItem, String> nameColumn = new TableColumn<>("Name");
    @FXML private javafx.scene.control.TableColumn<StockItem, String> descriptionColumn = new TableColumn<>("Description");
    @FXML private javafx.scene.control.TableColumn<StockItem, Double> priceColumn = new TableColumn<>("Price");
    @FXML private javafx.scene.control.TableColumn<StockItem, Integer> quantityColumn = new TableColumn<>("Quantity");
    @FXML private javafx.scene.control.TextField barCodeField;
    @FXML private javafx.scene.control.TextField amountField;
    @FXML private javafx.scene.control.TextField descriptionField;
    @FXML private javafx.scene.control.TextField nameField;
    @FXML private javafx.scene.control.TextField priceField;
    @FXML private Button refreshWarehousebutton;
    @FXML private Button addProductbutton;
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
        warehouseTableView.getColumns().addAll(idColumn,quantityColumn,nameColumn,descriptionColumn,priceColumn);
        // TODO refresh view after adding new items
    }
    @FXML public void refreshButtonClicked() {
        log.info("Refresh button clicked");
        refreshStockItems();
    }
    private void refreshStockItems() {
        warehouseTableView.setItems(new ObservableListWrapper<>(dao.findStockItems()));
        barCodeField.clear();
        nameField.clear();
        amountField.clear();
        priceField.clear();
        descriptionField.clear();
        warehouseTableView.refresh();
    }
    @FXML protected void addButtonClicked() {
        log.info("Add button clicked");
        System.out.println(barCodeField);
        if (barCodeField == null && nameField == null && descriptionField == null && priceField == null && amountField == null) {
            StockItem item_tobe_added = new StockItem();
            item_tobe_added.setId(getId());
            item_tobe_added.setName(getName());
            item_tobe_added.setDescription(getDescription());
            item_tobe_added.setPrice(getPrice());
            item_tobe_added.setQuantity(getQuantity());
            dao.saveStockItem(item_tobe_added);

        }else{
            log.info("Some field is missing. Item can not be added.");
        }
    }
    @FXML protected void removeButtonClicked(){
        log.info("Remove button clicked");
        if (barCodeField == null && nameField == null && descriptionField == null && priceField == null && amountField == null) {
            StockItem item_tobe_removed = new StockItem(getId(), getName(), getDescription(), getPrice(),getQuantity());
            dao.removeStockItem(item_tobe_removed);
            log.info("Item was removed. Table is ready to be refreshed.");
        }else{
            log.info("Some field is missing. Item can not be removed");
        }

    }
    @FXML protected void addAmount(){
        this.amountField = amountField;
    }
    @FXML protected void addBarcode(){
        this.barCodeField = barCodeField;
    }
    @FXML protected void addName(){
        this.nameField = nameField;
    }
    @FXML protected void addDesc(){
        this.descriptionField = descriptionField;
    }
    @FXML protected void addPrice(){
        this.priceField = priceField;
    }
    private int getQuantity (){
        return Integer.parseInt(amountField.getText());
    }
    private long getId () { return  Long.parseLong(barCodeField.getText());}
    private String getName () { return nameField.getText();}
    private double getPrice () { return Double.parseDouble(priceField.getText());}
    private String getDescription () { return descriptionField.getText();}
}
