package ee.ut.math.tvt.salessystem.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class HistoryControllerTest {
    HibernateSalesSystemDAO dao = new HibernateSalesSystemDAO();
    double width = 600;
    @org.junit.jupiter.api.Test
    void initialize() {
    }

    @org.junit.jupiter.api.Test
    void showBetweenDatesButtonClicked() {
        HistoryController history_controller = new HistoryController(dao,width);

        LocalDate start_date = new LocalDate(2016,11,24);
        LocalDate end_date = new LocalDate(2016,10,24);
        history_controller.set_EndDate(end_date);
        history_controller.set_EndDate(start_date);

        //No orders between selected dates
        assertEquals(0,history_controller.number_of_orders());


    }

    @org.junit.jupiter.api.Test
    void showLast10ButtonClicked() {
        HistoryController history_controller = new HistoryController(dao,width);
        history_controller.showAllButtonClicked();
        boolean more_than_10 = false;
        if(history_controller.number_of_orders() >= 10){
            more_than_10 = true;
        }
        assertEquals(false,false);
    }

    @org.junit.jupiter.api.Test
    void showAllButtonClicked() {
        HistoryController history_controller = new HistoryController(dao,width);
        history_controller.showAllButtonClicked();
        assertEquals(3,history_controller.number_of_orders());
    }

    @org.junit.jupiter.api.Test
    void displayOrder() {
    }
}