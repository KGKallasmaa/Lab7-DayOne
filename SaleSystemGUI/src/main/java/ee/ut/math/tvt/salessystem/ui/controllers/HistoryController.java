package ee.ut.math.tvt.salessystem.ui.controllers;

import com.sun.javafx.collections.ObservableListWrapper;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.security.PrivateKey;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.*;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
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
    @FXML private javafx.scene.control.TableColumn<SoldItem, Date> dateColumn= new TableColumn<>("Date");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> timeColumn= new TableColumn<>("Time");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> sumColumn= new TableColumn<>("Sum");
    @FXML private javafx.scene.control.TableView<SoldItem> orderTableView;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> idColumn= new TableColumn<>("Id");
    @FXML private javafx.scene.control.TableColumn<SoldItem, String> nameColumn= new TableColumn<>("Name");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> priceColumn= new TableColumn<>("Price");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Integer> quantityColumn= new TableColumn<>("Quantity");
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> order_sumColumn= new TableColumn<>("Sum");


    private ObservableList<SoldItem> data;

    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    public void initialize(URL location, ResourceBundle resources) {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setMinWidth(200);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setMinWidth(200);
        sumColumn.setCellValueFactory(new PropertyValueFactory<>("sum"));
        sumColumn.setMinWidth(200);
        historyTableView.getColumns().addAll(dateColumn,timeColumn,sumColumn);
        historyTableView.refresh();
    }

    @FXML protected void showBetweenDatesButtonClicked() {
        log.info("Show between dates button clicked");
    }

    @FXML protected void showLast10ButtonClicked() {
        log.info("Show last 10 button clicked");
    }

    @FXML protected void showAllButtonClicked(){
        log.info("Show all button clicked");
        HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
        List<SoldItem> orders = new ArrayList<>();
        for (Long e : all_orders.keySet()){
            Date date = new Date(e);
            Long time = e;
            double sum = 0;
            for(SoldItem el : all_orders.get(e)){
                sum += el.getSum();
            }
            SoldItem element = new SoldItem(date,time,sum);
            System.out.println(element.toString());
            orders.add(element);
        }
        log.info("Number of orders: "+orders.size());

        historyTableView.setItems(new ObservableListWrapper<>(orders));
        historyTableView.refresh();
    }

    @FXML protected void displayOrder() {
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
    @FXML protected void startDateFieldClicked(){
        log.info("Start button clicked");
    }
    @FXML protected void endDateFieldClicked(){
        log.info("End button clicked");
    }
}
