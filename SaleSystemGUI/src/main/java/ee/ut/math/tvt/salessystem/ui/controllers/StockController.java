package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {

    private final SalesSystemDAO dao;

    @FXML
    private Button addItem;
    @FXML
    private TableView<StockItem> warehouseTableView;
    private java.awt.TextField BarCodeField;
    private javafx.scene.control.TextField amountField;
    private javafx.scene.control.TextField nameField;
    private javafx.scene.control.TextField priceField;
    private Button refreshWarehouse;

    public StockController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshStockItems();
        // TODO refresh view after adding new items
    }

    @FXML
    public void refreshButtonClicked() {
        refreshStockItems();
    }

    private void refreshStockItems() {
        warehouseTableView.setItems(new ObservableListWrapper<>(dao.findStockItems()));
        warehouseTableView.refresh();
    }

    protected void addButtonClicked() {
    }
}
