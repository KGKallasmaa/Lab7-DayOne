package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
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
    @FXML private DatePicker startDateField;
    @FXML private DatePicker endDateField;
    @FXML private javafx.scene.control.Button showBetweenDates;
    @FXML private javafx.scene.control.Button showLast10;
    @FXML private javafx.scene.control.Button showAll;
    @FXML private javafx.scene.control.TableView<SoldItem> historyTableView;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Date> dateColumn = new TableColumn<>("Date");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> timeColumn = new TableColumn<>("Time");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> sumColumn = new TableColumn<>("Sum");
    @FXML private javafx.scene.control.TableView<SoldItem> orderTableView;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> idColumn = new TableColumn<>("Id");
    @FXML private javafx.scene.control.TableColumn<SoldItem, String> nameColumn = new TableColumn<>("Name");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> priceColumn = new TableColumn<>("Price");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Integer> quantityColumn = new TableColumn<>("Quantity");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> order_sumColumn = new TableColumn<>("Sum");

    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
    }
    public void initialize(URL location, ResourceBundle resources) {
        log.info("History tab initialized");
        //history tab
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setMinWidth(200);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setMinWidth(200);
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setMinWidth(200);
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
        idColumn.setMinWidth(120);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMinWidth(120);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setMinWidth(120);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setMinWidth(120);
        order_sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        order_sumColumn.setMinWidth(120);
        orderTableView.getColumns().addAll(idColumn,nameColumn,priceColumn,quantityColumn,order_sumColumn);

        orderTableView.refresh();
    }

    @FXML protected void showBetweenDatesButtonClicked() {
        log.info("Show between dates button clicked");
        historyTableView.getItems().clear();
        try{
            if(startDateField.getValue() == null || endDateField.getValue() == null){
                throw new NullPointerException();
            }
            HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
            List<SoldItem> orders = new ArrayList<>();
            LocalDate start =  startDateField.getValue();
            LocalDate end =  endDateField.getValue();
            Instant instant_1 = Instant.from(start.atStartOfDay(ZoneId.systemDefault()));
            Instant instant_2 = Instant.from(end.atStartOfDay(ZoneId.systemDefault()));
            Date date_start = Date.from(instant_1);
            Date date_end = Date.from(instant_2);
            if(date_start.getTime() <= date_end.getTime()) {
                for (Long e : all_orders.keySet()) {
                    Date date = new Date(e);
                    Long time = e;
                    double sum = 0;
                    for (SoldItem el : all_orders.get(e)) {
                        sum += el.getSum();
                    }
                    if (e >= date_start.getTime() && e <= date_end.getTime()) {
                        SoldItem element = new SoldItem(date, time, sum);
                        orders.add(element);
                    }
                }
                if (orders.size() < 1) {
                    log.warn("No orders between selected dates");
                } else {
                    log.info("Number of orders between selected dates: " + orders.size());
                    historyTableView.setItems(new ObservableListWrapper<>(orders));
                }
                historyTableView.refresh();
            }else{
                throw new IllegalArgumentException();
            }
        }catch (NullPointerException e){
            log.error("Start and end dates were not selected");
        }catch (IllegalArgumentException e){
            log.error("Start date is not before end date");
        }
    }
    @FXML protected void showLast10ButtonClicked() {
        log.info("Show last 10 button clicked");
        historyTableView.getItems().clear();
        HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
        //sorting keys by value
        Set<Long> sortable = all_orders.keySet();
        List<Long> sortable_keys = new ArrayList<>(sortable);
        Collections.sort(sortable_keys);
        Collections.reverse(sortable_keys);

        List<SoldItem> orders = new ArrayList<>();
        for (Long e : sortable_keys){
            Date date = new Date(e);
            Long time = e;
            double sum = 0;
            for(SoldItem el : all_orders.get(e)){
                sum += el.getSum();
            }
            if (orders.size() <= 10){
                SoldItem element = new SoldItem(date, time, sum);
                orders.add(element);
            }
        }
        if(orders.size() < 1){
            log.warn("No orders between selected dates");
        } else if(orders.size() < 10){
            log.warn("Total orders is lower than 10. Number of orders is "+orders.size());
        }
        else{
            log.info("Last 10 orders shown");
            historyTableView.setItems(new ObservableListWrapper<>(orders));
        }
        historyTableView.refresh();
    }
    @FXML protected void showAllButtonClicked(){
        log.info("Show all button clicked");
        historyTableView.getItems().clear();
        HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
        List<SoldItem> orders = new ArrayList<>();
        for (Long e : all_orders.keySet()){
            Date date = new Date(e);
            Long time = e.longValue();
            double sum = 0;
            for(SoldItem el : all_orders.get(e)){
                sum += el.getSum();
            }
            SoldItem element = new SoldItem(date,time,sum);
            orders.add(element);
        }
        if(orders.size() < 1){
            log.warn("NO orders found from database");
        }else{
            log.info("Total number of orders: "+orders.size());
            historyTableView.setItems(new ObservableListWrapper<>(orders));
        }

        historyTableView.setItems(new ObservableListWrapper<>(orders));
        historyTableView.refresh();
    }
    @FXML protected void displayOrder(SoldItem purchaseOrder) {
        Long thisOrderTime = purchaseOrder.getTime();
        HashMap<Long, List<SoldItem>> orders = dao.findAllOrders();
        List<SoldItem> thisOrderList = orders.get(thisOrderTime);
        orderTableView.setItems(new ObservableListWrapper<>(thisOrderList));
        orderTableView.refresh();
    }

    @FXML protected void startDateFieldClicked(){
        log.info("Start date = "+startDateField.getValue());
    }
    @FXML protected void endDateFieldClicked(){
        log.info("End date = "+endDateField.getValue());
    }
}
