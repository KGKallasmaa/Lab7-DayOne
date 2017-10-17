package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Date;
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


    public HistoryController(SalesSystemDAO dao) {
        this.dao = dao;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: implement
    }

    @FXML protected void showBetweenDatesButtonClicked(Date startDateField, Date endDateFiled) {
    }

    @FXML protected void showLast10ButtonClicked() {
    }

    @FXML protected void showAll() {
    }

    @FXML protected void displayOrder() {
    }
}
