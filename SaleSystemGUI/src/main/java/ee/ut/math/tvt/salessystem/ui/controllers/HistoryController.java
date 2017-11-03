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
import java.util.*;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    @FXML private javafx.scene.control.TableColumn<SoldItem, Date> dateColumn;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Long> timeColumn;
    @FXML private javafx.scene.control.TableColumn<SoldItem, Double> sumColumn;
    private ObservableList<SoldItem> data;

    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML protected void showBetweenDatesButtonClicked() {
        log.info("Show between dates button clicked");
    }

    @FXML protected void showLast10ButtonClicked() {

    }

    @FXML protected void showAllButtonClicked(){
        log.info("Show all button clicked");
        if(startDateField != null && endDateField != null){
            //populating table
            TableView table_view = new TableView<SoldItem>();

            //all shoping bags
            HashMap<Date,List<SoldItem>> bags = dao.getSoldItemMap();

            //suitable shoping bags
            List<SoldItem> orders = new ArrayList<>();
            for(Date e : bags.keySet()){
                Date date = e;
                Long time = e.getTime();
                double sum = 0;
                for(SoldItem el : bags.get(e)){
                    sum += el.getSum();
                }
                SoldItem neww = new SoldItem(date.toString(),time.toString(),sum);
                orders.add(neww);
            }


            table_view.setItems(new ObservableListWrapper<>(orders));
         //   System.out.println("Order list: "+dao.getOrder_list().toString());
            historyTableView = table_view;
            historyTableView.refresh();

    }else{
            log.info("Start or end date is not selected");
        }
    }

    @FXML protected void displayOrder() {
    }
    @FXML protected void startDateFieldClicked(){
        log.info("Start button clicked");
    }
    @FXML protected void endDateFieldClicked(){
        log.info("End button clicked");
    }
}
