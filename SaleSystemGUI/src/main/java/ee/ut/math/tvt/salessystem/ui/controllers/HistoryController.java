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
        if(startDateField != null && endDateField != null){
            HashMap<Long,List<SoldItem>> orders = dao.getSoldItemMap();
            HashMap<Long,List<SoldItem>> suitable_orders = new HashMap<>();
            //Removing elements that are not suitable
            Date start_date = new Date(startDateField.getValue().toEpochDay());
            Date end_date = new Date(endDateField.getValue().toEpochDay());

            for(Long time : orders.keySet()){
                if(time > start_date.getTime() && time < end_date.getTime()){
                    suitable_orders.put(time,orders.get(time));
                }
            }
            List<Date> dates = new ArrayList<>();
            List<Long> times = new ArrayList<>();
            List<Integer> sums = new ArrayList<>();
            for(Long time : suitable_orders.keySet()){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                dates.add(calendar.getTime());
                times.add(time);
                int sum = 0;
                List<SoldItem> elements = suitable_orders.get(time);
                for(SoldItem el : elements){
                    sum += el.getSum();
                }
                sums.add(sum);
            }

        }
    }

    @FXML protected void showLast10ButtonClicked() {

    }
    private List<SoldItem> order_list(HashMap<Long,List<SoldItem>> suitable_orders) {
       List<SoldItem> data = new ArrayList<>();
        for(Long time : suitable_orders.keySet()){

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            Date date = calendar.getTime();
            double sum = 0;
            List<SoldItem> elements = suitable_orders.get(time);
            for(SoldItem element : elements) {
                sum += element.getSum();
            }

            SoldItem el = new SoldItem(date.toString(),time.toString(),sum);
            data.add(el);
            System.out.println("info: "+el);
        }
        System.out.println(data);
        return data;
    }

    @FXML protected void showAllButtonClicked(){
        log.info("Show all button clicked");

        System.out.println(startDateField);
        if(startDateField != null && endDateField != null){
            HashMap<Long,List<SoldItem>> orders = dao.getSoldItemMap();
            HashMap<Long,List<SoldItem>> suitable_orders = new HashMap<>();
            //Removing elements that are not suitable
            Date start_date = new Date(startDateField.getValue().toEpochDay());
            Date end_date = new Date(endDateField.getValue().toEpochDay());

            for(Long time : orders.keySet()){
                suitable_orders.put(time,orders.get(time));
            }
            //System.out.println(orders);


            List<Date> dates = new ArrayList<>();
            List<Long> times = new ArrayList<>();
            List<Integer> sums = new ArrayList<>();
            for(Long time : suitable_orders.keySet()){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                dates.add(calendar.getTime());
                times.add(time);
                int sum = 0;
                List<SoldItem> elements = suitable_orders.get(time);
                for(SoldItem el : elements){
                    sum += el.getSum();
                }
                sums.add(sum);
            }

            System.out.println(suitable_orders);
            //System.out.println(dates);
            //System.out.println(times);
            //System.out.println(sums);

            //populating table

            data = FXCollections.observableArrayList();
            ObservableList<Map> allData = FXCollections.observableArrayList();
            int i = 0;
            while (i<dates.size()){
                Map<String,String> dataRow = new HashMap<>();
                String date = dates.get(i).toString() ;
                String time = times.get(i).toString();
                String sum= sums.get(i).toString();
                dataRow.put(dateColumn.getText(),date);
                dataRow.put(timeColumn.getText(),time);
                dataRow.put(sumColumn.getText(),sum);
                allData.add(dataRow);
                i++;
            }

            TableView table_view = new TableView<SoldItem>();
            table_view.setItems(new ObservableListWrapper<>(order_list(suitable_orders)));
            historyTableView = table_view;
            System.out.println(table_view.getItems());
            System.out.println(order_list(suitable_orders));
//            table_view.setEditable(true);
//            table_view.getSelectionModel().setCellSelectionEnabled(true);
//            table_view.getColumns().setAll(dateColumn, timeColumn, sumColumn);

            /*
            for(Long l : suitable_orders.keySet()){
                List<SoldItem> el = suitable_orders.get(l);
                data.add(el);
            }
            */


            //Adding items to columns is broken!!!
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
