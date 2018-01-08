package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.PrivateKey;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {
    private final SalesSystemDAO dao;
    private static final Logger log = LogManager.getLogger(HistoryController.class);
    private double width;
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private javafx.scene.control.Button showBetweenDates;
    @FXML private javafx.scene.control.Button showLast10;
    @FXML private javafx.scene.control.Button showAll;
    @FXML private javafx.scene.control.TableView<SoldItem> historyTableView;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Date> dateColumn = new TableColumn<>("Date");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> timeColumn = new TableColumn<>("Time");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> sumColumn = new TableColumn<>("Sum");
    @FXML private javafx.scene.control.TableView<StockItem> orderTableView;
    @FXML private javafx.scene.control.TableColumn<StockItem, Long> idColumn = new TableColumn<>("Id");
    @FXML private javafx.scene.control.TableColumn<StockItem, String> nameColumn = new TableColumn<>("Name");
    @FXML private javafx.scene.control.TableColumn<StockItem, Double> priceColumn = new TableColumn<>("Price");
    @FXML private javafx.scene.control.TableColumn<StockItem, Integer> quantityColumn = new TableColumn<>("Quantity");
    @FXML private javafx.scene.control.TableColumn<StockItem, Double> order_sumColumn = new TableColumn<>("Sum");

    //For testing
    public int number_of_orders(){
        return historyTableView.getItems().size();
    }
    public HistoryController(SalesSystemDAO dao,double width) {
        this.dao = dao;
        this.width = width;
    }
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("History tab initialized");
        //history tab
        double order_width = this.width/3;
        double order_sum_width = this.width/5;
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setMinWidth(order_width);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setMinWidth(order_width);
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setMinWidth(order_width);
        historyTableView.getColumns().addAll(dateColumn,timeColumn,sumColumn);
        historyTableView.refresh();

        //order tab
        //sets order table when an order table row is clicked to that order
        historyTableView.setRowFactory( tv -> {
            TableRow<SoldItem> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {
                    SoldItem rowData = row.getItem();
                    //System.out.println(rowData);
                    displayOrder(rowData);
                }
            });
            return row ;
        });
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMinWidth(order_sum_width);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(order_sum_width);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setMinWidth(order_sum_width);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(order_sum_width);
        order_sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        order_sumColumn.setMinWidth(order_sum_width);
        orderTableView.getColumns().addAll(idColumn,nameColumn,priceColumn,quantityColumn,order_sumColumn);

        orderTableView.refresh();
    }

    @FXML protected void showBetweenDatesButtonClicked() {
        try{
            log.debug("Show between dates button clicked");
            historyTableView.getItems().clear();
            orderTableView.getItems().clear();
            HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
            List<SoldItem> orders = new ArrayList<>();
            for (Long e : all_orders.keySet()){
                Date date = new Date(e);
                Long time = e;
                double sum = 0;
                for(SoldItem el : all_orders.get(e)){
                    if (dao.findStockItem(el.getStockItem_id()) != null){
                        sum += dao.findStockItem(el.getStockItem_id()).getPrice()*el.getQuantity();
                    }
                }
                SoldItem element = new SoldItem(date, time, sum);
                LocalDate start = startDateField.getValue();
                LocalDate end = endDateField.getValue();
                if (start == null && end == null){
                    String msg = "Start date and end date were not found";
                    throw new IllegalArgumentException(msg);
                }
                if (startDateField.getValue() != null && endDateField.getValue() != null){
                    Instant instant_1 = Instant.from(start.atStartOfDay(ZoneId.systemDefault()));
                    Instant instant_2 = Instant.from(end.atStartOfDay(ZoneId.systemDefault()));
                    Date start_date = Date.from(instant_1);
                    Date end_date  = Date.from(instant_2);
                    if(end_date.getTime() < start_date.getTime()){
                        String msg = "Start date was not before end date";
                        throw new IllegalArgumentException(msg);
                    }
                }
                if (startDateField.getValue() == null){
                    String msg = "Start date was not found";
                    throw new IllegalArgumentException(msg);
                }
                if (endDateField.getValue() == null){
                    String msg = "End date was not found";
                    throw new IllegalArgumentException(msg);
                }
                Instant instant_1 = Instant.from(start.atStartOfDay(ZoneId.systemDefault()));
                Instant instant_2 = Instant.from(end.atStartOfDay(ZoneId.systemDefault()));
                Date start_date = Date.from(instant_1);
                Date end_date  = Date.from(instant_2);

                if (date.getTime() > start_date.getTime() && date.getTime() <= end_date.getTime()){
                    orders.add(element);
                }
            }
            if(orders.size() < 1){
                log.warn("NO orders found from database");
            }else{
                log.debug("Total number of orders: "+orders.size());
                historyTableView.setItems(new ObservableListWrapper<>(orders));
                log.info("All orders are being shown");
            }
            historyTableView.setItems(new ObservableListWrapper<>(orders));
        }
        catch (IllegalArgumentException e){
            log.error(e);
        }
        finally {
            historyTableView.refresh();
        }
    }

    @FXML protected void showLast10ButtonClicked() {
        log.debug("Show last 10 button clicked");
        historyTableView.getItems().clear();
        orderTableView.getItems().clear();
        HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
        List<SoldItem> orders = new ArrayList<>();

        ArrayList<Long> all_keys = new ArrayList<>();
        all_keys.addAll(all_orders.keySet());
        Collections.sort(all_keys);
        Collections.reverse(all_keys);

        for (Long e : all_keys){
            Date date = new Date(e);
            Long time = e;
            double sum = 0;
            for(SoldItem el : all_orders.get(e)){
                sum += dao.findStockItem(el.getStockItem_id()).getPrice()*el.getQuantity();
            }
            SoldItem element = new SoldItem(date, time, sum);
            if (orders.size() < 10){
                orders.add(element);
            }
        }
        if(orders.size() < 1){
            log.warn("NO orders found from database");
        }
        else if (orders.size() < 10){
            log.debug("Total number of orders was under 10");
        }
        else{
            log.debug("Total number of orders: "+orders.size());
            log.info("All orders are being shown");
        }

        historyTableView.setItems(new ObservableListWrapper<>(orders));
        historyTableView.refresh();
    }
    @FXML protected void showAllButtonClicked(){
        log.debug("Show all button clicked");
        historyTableView.getItems().clear();
        orderTableView.getItems().clear();
        HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
        List<SoldItem> orders = new ArrayList<>();
        for (Long e : all_orders.keySet()){
            Date date = new Date(e);
            Long time = e;
            double sum = 0;
            for(SoldItem el : all_orders.get(e)){
                sum += dao.findStockItem(el.getStockItem_id()).getPrice()*el.getQuantity();
            }
            SoldItem element = new SoldItem(date, time, sum);
            orders.add(element);
        }
        if(orders.size() < 1){
            log.warn("NO orders found from database");
        }else{
            log.debug("Total number of orders: "+orders.size());
            historyTableView.setItems(new ObservableListWrapper<>(orders));
            log.info("All orders are being shown");
        }

        historyTableView.setItems(new ObservableListWrapper<>(orders));
        historyTableView.refresh();
    }
    @FXML protected void displayOrder(SoldItem purchaseOrder) {
        Long thisOrderTime = purchaseOrder.getTime();
        HashMap<Long, List<SoldItem>> orders = dao.findAllOrders();
        List<SoldItem> all_solditems = dao.findSoldItems();
        HashMap<SoldItem,Long> solditem_stockitemid = new HashMap<>();
        for(SoldItem el : all_solditems){
            solditem_stockitemid.put(el,el.getStockItem_id());
        }
   //    public SoldItem(Long id, Long time,Long stockItem_id, int quantity) {
        List<StockItem> thisOrderList = new ArrayList<>();
        for(SoldItem el : orders.get(thisOrderTime)){
            StockItem exploited_item = dao.findStockItem(solditem_stockitemid.get(el));
            StockItem item = new StockItem(exploited_item.getId(),exploited_item.getName(),exploited_item.getDescription(),exploited_item.getPrice(),el.getQuantity(),exploited_item.getPrice()*el.getQuantity());
            thisOrderList.add(item);
        }
     //   StockItem(Long id, String name, String description,Double price, int quantity) {
        orderTableView.setItems(new ObservableListWrapper<>(thisOrderList));
        orderTableView.refresh();
    }

    @FXML protected void startDateFieldClicked(){
        log.debug("Start date = "+startDateField.getValue());
    }
    @FXML protected void endDateFieldClicked(){
        log.debug("End date = "+endDateField.getValue());
    }
    public void set_StartDate(LocalDate startDate){
        this.startDateField.setValue(startDate);
    }
    public void set_EndDate(LocalDate endDate){
        this.endDateField.setValue(endDate);
    }


}
