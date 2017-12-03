package ee.ut.math.tvt.salessystem.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import java.time.ZoneId;

class HistoryControllerTest {
    HibernateSalesSystemDAO dao = new HibernateSalesSystemDAO();
    double width = 600;

    HistoryController history_controller = new HistoryController(dao,width);

    @Test
    void showBetweenDatesButtonClicked() {

        Long end_time = 1511863473633L-365*86400000; //2016/11/28
        Long start_time = end_time-7*86400000L; // 2016/11/21

        LocalDate start_date =
                Instant.ofEpochMilli(start_time).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end_date =
                Instant.ofEpochMilli(end_time).atZone(ZoneId.systemDefault()).toLocalDate();


        history_controller.set_StartDate(start_date);
        history_controller.set_EndDate(end_date);

        //No orders between selected dates
        assertEquals(history_controller.number_of_orders(), 0);

        //1 order between the dates
        end_time = end_time+365*86400000; //2017/11/28
        start_time = end_time-7*86400000L; // 2017/11/21

        //All orders between the dates
        assertEquals(history_controller.number_of_orders(), 1);

        //Error: start date not selected
        start_date = null;
        history_controller.set_StartDate(start_date);
        assertEquals(history_controller.number_of_orders(), 0);

        //Error: end date not selected

        end_date = null;
        history_controller.set_EndDate(end_date);
        assertEquals(history_controller.number_of_orders(), 0);

        //Error: start and end date not selected
        start_date = null;
        end_date = null;
        history_controller.set_StartDate(start_date);
        history_controller.set_EndDate(end_date);
        assertEquals(history_controller.number_of_orders(), 0);

        //Errorr: start date is not before end date

        end_time = end_time+365*86400000; //2017/11/28
        start_time = end_time+7*86400000L; // 2017/11/21
        start_date =
                Instant.ofEpochMilli(start_time).atZone(ZoneId.systemDefault()).toLocalDate();
        end_date =
                Instant.ofEpochMilli(end_time).atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(history_controller.number_of_orders(), 0);


    }

    @Test
    void showLast10ButtonClicked() {
        history_controller.showAllButtonClicked();
        boolean more_than_10 = false;
        if(history_controller.number_of_orders() >= 10){
            more_than_10 = true;
        }
        //Number of orders is <= 10
        assertEquals(false,false);



        //Display orders without start and end date

        Long start_time = null;
        Long end_time = null;
        LocalDate start_date =
                Instant.ofEpochMilli(start_time).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end_date =
                Instant.ofEpochMilli(end_time).atZone(ZoneId.systemDefault()).toLocalDate();
        history_controller.set_StartDate(start_date);
        history_controller.set_EndDate(end_date);
        assertEquals(history_controller.number_of_orders(),3);
    }

    @Test
    void showAllButtonClicked() {
        history_controller.showAllButtonClicked();
        assertEquals(3,history_controller.number_of_orders());

        //Display orders without the need for start and end date
        Long start_time = null;
        Long end_time = null;
        LocalDate start_date =
                Instant.ofEpochMilli(start_time).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end_date =
                Instant.ofEpochMilli(end_time).atZone(ZoneId.systemDefault()).toLocalDate();
        history_controller.set_StartDate(start_date);
        history_controller.set_EndDate(end_date);
        assertEquals(history_controller.number_of_orders(),3);
    }

}