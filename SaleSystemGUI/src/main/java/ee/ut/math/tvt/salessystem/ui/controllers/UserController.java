package ee.ut.math.tvt.salessystem.ui.controllers;


import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.ui.SalesSystemUI;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.scene.control.CheckBox;

public class UserController  implements Initializable {

    private final SalesSystemDAO dao;
  //  private static final Logger log = (Logger) LogManager.getLogger(UserController.class);
    @FXML private javafx.scene.control.CheckBox cashierbox;
    @FXML private javafx.scene.control.CheckBox warehousebox;
    @FXML private javafx.scene.control.CheckBox adminbox;
    @FXML private javafx.scene.control.Button setuser;
    public UserController(SalesSystemDAO dao) {
        this.dao = dao;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML public void setCashierbox (){
        this.cashierbox.setSelected(true);
        this.warehousebox.setSelected(false);
        this.adminbox.setSelected(false);
    }
    @FXML public void setWarehousebox (){
        this.warehousebox.setSelected(true);
        this.cashierbox.setSelected(false);
        this.adminbox.setSelected(false);
    }
    @FXML public void setAdminbox (){
        this.warehousebox.setSelected(false);
        this.cashierbox.setSelected(false);
        this.adminbox.setSelected(true);
    }
    @FXML public void selectUser (){
        if (adminbox.isSelected()){
            dao.setUser("Admin");
            tab_handler();
        }
        if (warehousebox.isSelected()){
            dao.setUser("Warehouse");
            tab_handler();
        }
        if (cashierbox.isSelected()){
            dao.setUser("Cashier");
            tab_handler();
        }
    }
    private void tab_handler (){
        TabPane current_tab = dao.getTabs();
        switch (dao.getUser()){
            case ("Admin"):
                current_tab.getTabs().removeAll();
                SalesSystemUI ui = new SalesSystemUI();
                current_tab.getTabs().addAll(ui.getInizal_tabs());
                ui.refrech();
                break;
            case("Warehouse"):
                current_tab.getTabs().removeAll();
                SalesSystemUI ui_1 = new SalesSystemUI();
                current_tab.getTabs().addAll(ui_1.getWarehouse_tabs());
                ui_1.refrech();
                break;
            case ("Cashier"):
                current_tab.getTabs().removeAll();
                SalesSystemUI ui_2 = new SalesSystemUI();
                current_tab.getTabs().addAll(ui_2.getCashier_tabs());
                ui_2.refrech();
                break;
        }
    }
}
