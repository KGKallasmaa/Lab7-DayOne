package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private Button fullScreenButton ;
    private double width;

    public StockController(SalesSystemDAO dao, double width) {

        this.dao = dao;
        this.width = width;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {

        double width = this.width/5;
        log.debug("Stock tab initialized");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(width);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(width);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(width);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(width);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setPrefWidth(width);
        warehouseTableView.getColumns().addAll(idColumn,nameColumn,descriptionColumn,priceColumn,quantityColumn);
        refreshStockItems();

        /* // lambda for remove product, not integrated
        removeproductButton.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                    if (!newPropertyValue) {

                    }
                }
        });
        */
    }

    public List<Long> all_ids (){
        List<StockItem> el = dao.findStockItems();
        List<Long> ids = new ArrayList<>();
        for(StockItem item : el){
            ids.add(item.getId());
        }
        return ids;
    }

    @FXML public void refreshButtonClicked() {
        log.debug("Refresh button clicked");
        refreshStockItems();
    }

    @FXML protected void addButtonClicked() {
        log.debug("Add button clicked");
        //filtering unsuitable values
        try {
            if(!nameField.getText().isEmpty() && !priceField.getText().isEmpty() && !amountField.getText().isEmpty()) {
                Long bar = null;
                if (barCodeField.getText().isEmpty()){
                    log.warn("ID field is empty. Generating new value");
                    int i = 0;
                    Long generated_id = Long.valueOf(all_ids().size()+1+i);
                    while (true){
                        if (dao.findStockItem(generated_id) == null){
                            break;
                        }
                        generated_id = Long.valueOf(all_ids().size()+1+i);
                        i++;
                    }
                    log.warn("New id "+generated_id);
                    bar = generated_id;
                }else{
                    bar = Long.parseLong(barCodeField.getText());
                }
                if (descriptionField.getText().equals("")) {
                    descriptionField.setText(nameField.getText());
                }
                StockItem item_tobe_added = new StockItem(bar, nameField.getText(), descriptionField.getText(),
                        Double.parseDouble(priceField.getText()), Integer.parseInt(amountField.getText()));
                if (item_tobe_added.getQuantity() > 0 && item_tobe_added.getPrice() >= 0) {
                    // PRICE HAS TO BE NOT NEGATIVE.
                    dao.saveStockItem(item_tobe_added);
                    log.info("Item was added to the warehouse");
                } else if (item_tobe_added.getQuantity() <= 0) {
                    log.error(item_tobe_added.getName() + " Quantity has to be above 0");
                    JOptionPane.showMessageDialog(null, "Quantity must be above 0", "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else if (item_tobe_added.getPrice() < 0) {
                    log.error(item_tobe_added.getName() + " price can not be negative");
                }
            }
        }catch (NumberFormatException e){
            log.error("Invalid inputs in some fields");
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        finally {
            clearAll();
        }
    }
    public void clearAll(){
        barCodeField.clear();
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        amountField.clear();
    }
    @FXML public void removeButtonClicked() {
        log.debug("Remove button clicked");
        try {
            Long id = Long.parseLong(barCodeField.getText());
            List<StockItem> all_items = dao.findStockItems();
            List<Long> all_ids = new ArrayList<>();
            for (StockItem el : all_items) {
                all_ids.add(el.getId());
            }
            if (all_ids.contains(id)) {
                for (StockItem el : all_items) {
                    dao.beginTransaction();
                    if (el.getId() == id) {
                        StockItem warehouse_item = dao.findStockItem(id);
                        StockItem remove_item = new StockItem(warehouse_item.getId(), warehouse_item.getName(), warehouse_item.getDescription(), warehouse_item.getPrice(), Integer.parseInt(amountField.getText()));
                        if (remove_item.getQuantity() > 0 && remove_item.getPrice() >= 0) {
                            dao.removeStockItem(remove_item, true);
                            log.info(remove_item.getName() + " was removed to the warehouse");
                        } else if (remove_item.getQuantity() == 0) {
                            log.error(remove_item.getName() + " quantity can not be 0");
                        }
                        else if (remove_item.getPrice() < 0){
                            log.error(remove_item.getName()+" price can not be negative");
                        }
                        else {
                            log.error(remove_item.getName() + " quantity can not be 0");
                        }
                    }
                    dao.commitTransaction();
                }
            }else{
                throw new NullPointerException();
            }
        }
        catch(NullPointerException e){
            log.error("Barcode was not found");
            clearAll();
        }
        catch (IllegalArgumentException e){
            log.error("No Id entered");
            JOptionPane.showMessageDialog(null, "Item can not be removed without barcode", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        finally {
            clearAll();
        }
    }

    private void refreshStockItems() {
        log.info("Warehouse refreshed");
        List<StockItem> suitable_stockitems = new ArrayList<>();
        for(StockItem item : dao.findStockItems()) {
            if (item.getQuantity() != 0) {
                suitable_stockitems.add(item);
            }
        }
        warehouseTableView.setItems(new ObservableListWrapper<>(suitable_stockitems));

        if (suitable_stockitems.size() == 0){
            List<StockItem> suitable_stockitems_joke = new ArrayList<>();
            suitable_stockitems_joke.add(new StockItem(1000L,"I'm empty."," Please fill me!",0.0,0));
            warehouseTableView.setItems(new ObservableListWrapper<>(suitable_stockitems_joke));
        }

        warehouseTableView.refresh();
    }
    @FXML protected void addAmount(){
        log.debug("Amount selected");
        this.amountField = amountField;
    }
    @FXML protected void addBarcode(){
        log.debug("Barcode selected");
        this.barCodeField = barCodeField;
    }
    @FXML protected void addName(){
        log.debug("Name selected");
        this.nameField = nameField;
    }
    @FXML protected void addDesc(){
        log.debug("Description selected");
        this.descriptionField = descriptionField;
    }
    @FXML protected void addPrice(){
        log.debug("Price selected");
        this.priceField = priceField;
    }



    public int getQuantity () { return Integer.parseInt(amountField.getText());}
    public long getId () { return  Long.parseLong(barCodeField.getText());}
    public String getName () { return nameField.getText();}
    public double getPrice () { return Double.parseDouble(priceField.getText());}
    public String getDescription () { return descriptionField.getText();}

 //   public void setQuantity (Integer quantity){this.amountField.setText(quantity.toString());}
 //   public void setId (Long id){this.barCodeField.setText(id.toString());}
 //   public void setName (String name){this.nameField.setText(name);}
 //   public void setPrice (Double price){this.priceField.setText(price.toString());}
 //   public void setDescription (String description){this.descriptionField.setText(description.toString());}


}
