package ee.ut.math.tvt.salessystem.ui.controllers;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Encapsulates everything that has to do with the purchase tab (the tab
 * labelled "History" in the menu).
 */
public class HistoryController implements Initializable {
    private javafx.scene.control.TextField startDateField;
    private javafx.scene.control.TextField endDateField;
    private javafx.scene.control.Button showBetweenDates;
    private javafx.scene.control.Button showLast10;
    private javafx.scene.control.Button showAll;
    private javafx.scene.control.TableView historyTableView;
    private javafx.scene.control.TableView purchaseTableVew;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: implement
    }

    protected void showBetweenDatesButtonClicked(Date startDateField, Date endDateFiled) {
    }

    protected void showLast10ButtonClicked() {
    }

    protected void showAll() {
    }

    private void displayOrder() {
    }
}
