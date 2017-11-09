package ee.ut.math.tvt.salessystem.ui.controllers;


import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.*;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "Point-of-sale" in the menu). Consists of the purchase menu,
 * current purchase dialog and shopping cart table.
 */
public class PurchaseController implements Initializable {

    private static final Logger log = LogManager.getLogger(PurchaseController.class);

    private SalesSystemDAO dao;
    private ShoppingCart shoppingCart;

    @FXML private Button newPurchase;
    @FXML private Button submitPurchase;
    @FXML private Button cancelPurchase;
    @FXML private TextField barCodeField;
    @FXML private TextField quantityField;
    @FXML private ComboBox nameSelect;
    @FXML private TextField priceField;
    @FXML private Button addItemButton;
    @FXML private TableView<SoldItem> purchaseTableView;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> IdColumn = new TableColumn<>("Id");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Integer> QuantityColumn = new TableColumn<>("Quantity");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> PriceColumn = new TableColumn<>("Price");
    @FXML private javafx.scene.control.TableColumn<SoldItem, String> NameColumn = new TableColumn<>("Name");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> SumColumn = new TableColumn<>("Sum");


    public PurchaseController(SalesSystemDAO dao, ShoppingCart shoppingCart) {
        this.dao = dao;
        this.shoppingCart = shoppingCart;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Purchase tab initialized");
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        SumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));

        purchaseTableView.setItems(new ObservableListWrapper<>(shoppingCart.getAll()));
        purchaseTableView.getColumns().addAll(IdColumn, NameColumn, PriceColumn, QuantityColumn, SumColumn);
        disableProductField(true);
    }

    /** Event handler for the <code>new purchase</code> event. */
    @FXML
    protected void newPurchaseButtonClicked() {
        log.debug("New sale process started");
        try {
            enableInputs();
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Event handler for the <code>cancel purchase</code> event.
     */
    @FXML
    protected void cancelPurchaseButtonClicked() {
        log.info("Sale cancelled");
        try {
            shoppingCart.cancelCurrentPurchase();
            disableInputs();
            purchaseTableView.refresh();
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Event handler for the <code>submit purchase</code> event.
     */
    @FXML
    protected void submitPurchaseButtonClicked() {
        log.info("Submission process started");
        try {
            log.debug("Contents of the current basket:\n" + shoppingCart.getAll());
            shoppingCart.submitCurrentPurchase();
            disableInputs();
            purchaseTableView.refresh();
            log.info("Sale complete");
        } catch (SalesSystemException e) {
            log.error(e.getMessage(), e);
        }
    }

    // switch UI to the state that allows to proceed with the purchase
    private void enableInputs() {
        resetProductField();
        disableProductField(false);
        cancelPurchase.setDisable(false);
        submitPurchase.setDisable(false);
        newPurchase.setDisable(true);
        priceField.setDisable(true);
        barCodeField.setDisable(true);
    }

    // switch UI to the state that allows to initiate new purchase
    private void disableInputs() {

        resetProductField();
        cancelPurchase.setDisable(true);
        submitPurchase.setDisable(true);
        newPurchase.setDisable(false);
        disableProductField(true);
    }

    private StockItem fillInputsBySelectedStockItem() {

        List<StockItem> items = dao.findStockItems();
        String itemname = String.valueOf(nameSelect.getValue());
        log.info("Item "+itemname+" selected from dropdown menu");
        return dao.findStockItemName(itemname);

        }


    // Search the warehouse for a StockItem with the bar code entered
    // to the barCode textfield.
    private StockItem getStockItemByBarcode() {
        try {
            long code = Long.parseLong(barCodeField.getText());
            return dao.findStockItem(code);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Add new item to the cart.
     */
    @FXML
    public void addItemEventHandler() {
        // add chosen item to the shopping cart.
        StockItem stockItem = fillInputsBySelectedStockItem();
        barCodeField.setText(stockItem.getId().toString());
        priceField.setText(Double.toString(stockItem.getPrice()));
        if (stockItem != null) {
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
            } catch (NumberFormatException e) {
                quantity = 1;
            }

            shoppingCart.addItem(new SoldItem(stockItem, quantity));
            purchaseTableView.refresh();
        }
    }

    /**
     * Sets whether or not the product component is enabled.
     */
    private void disableProductField(boolean disable) {
        this.addItemButton.setDisable(disable);
        this.barCodeField.setDisable(disable);
        this.quantityField.setDisable(disable);
        this.nameSelect.setDisable(disable);
        this.priceField.setDisable(disable);
    }

    /**
     * Reset dialog fields.
     */
    private void resetProductField() {
        barCodeField.setText("");
        quantityField.setText("");
        priceField.setText("");
        //List<StockItem> items = dao.findStockItems();
        //nameSelect.setItems(new ObservableListWrapper(items));
        dropdown();
    }

    private void dropdown() {
        List<StockItem> items = dao.findStockItems();
        List<String> items2 = new ArrayList<>();
        for (StockItem thing : items) {
            items2.add(thing.getName());
        }
     //   System.out.print(items2);
        nameSelect.setItems(new ObservableListWrapper(items2));
    }

}
