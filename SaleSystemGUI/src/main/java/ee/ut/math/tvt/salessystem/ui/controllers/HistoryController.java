package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {
    private final SalesSystemDAO dao;
    @FXML private javafx.scene.control.DatePicker startDateField;
    @FXML private javafx.scene.control.DatePicker endDateField;
    @FXML private javafx.scene.control.Button showBetweenDates;
    @FXML private javafx.scene.control.Button showLast10;
    @FXML private javafx.scene.control.Button showAll;
    @FXML private javafx.scene.control.TableView historyTableView;
    @FXML private javafx.scene.control.TableView purchaseTableVew;
    @FXML private javafx.scene.control.TableColumn dateColumn;
    @FXML private javafx.scene.control.TableColumn timeColumn;
    @FXML private javafx.scene.control.TableColumn sumColumn;


    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML protected void showBetweenDatesButtonClicked() {

    }

    @FXML protected void showLast10ButtonClicked() {
    }

    @FXML protected void showAllButtonClicked(){
        List<StockItem> universe = dao.findStockItems();
    }

    @FXML protected void displayOrder() {
    }
}
