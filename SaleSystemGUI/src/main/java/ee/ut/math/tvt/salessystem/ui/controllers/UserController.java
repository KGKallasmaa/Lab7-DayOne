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
import javafx.scene.control.CheckBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserController  implements Initializable {

    private final SalesSystemDAO dao;
    private static final Logger log = LogManager.getLogger(UserController.class);
    @FXML private javafx.scene.control.CheckBox cashierbox;
    @FXML private javafx.scene.control.CheckBox warehousebox;
    @FXML private javafx.scene.control.CheckBox adminbox;
    @FXML private javafx.scene.control.TabPane tabpane;
    @FXML private javafx.scene.control.Tab stocktab;
    @FXML private javafx.scene.control.Tab usertab;
    @FXML private javafx.scene.control.Tab purchasetab;
    @FXML private javafx.scene.control.Tab historytab;
    @FXML private javafx.scene.control.Tab teamtab;

    public UserController(SalesSystemDAO dao, Tab usertab,Tab stocktab,Tab purchasetab,Tab historytab,Tab teamtab) {
        this.dao = dao;
        this.usertab = usertab;
        this.stocktab = stocktab;
        this.purchasetab = purchasetab;
        this.historytab = historytab;
        this.teamtab= teamtab;
    }

    @Override public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML public void setCashierbox (){
        log.info("Usertype = cashier");
        this.cashierbox.setSelected(true);
        this.warehousebox.setSelected(false);
        this.adminbox.setSelected(false);
        selectUser();
    }
    @FXML public void setWarehousebox (){
        log.info("Usertype = warehouse");
        this.warehousebox.setSelected(true);
        this.cashierbox.setSelected(false);
        this.adminbox.setSelected(false);
        selectUser();
    }
    @FXML public void setAdminbox (){
        log.info("Usertype = admin");
        this.warehousebox.setSelected(false);
        this.cashierbox.setSelected(false);
        this.adminbox.setSelected(true);
        selectUser();
    }
    @FXML public void selectUser (){
        if (adminbox.isSelected()){
            log.info("Admin rights enabled");
            historytab.setDisable(false);
            purchasetab.setDisable(false);
            stocktab.setDisable(false);
        }
        if (warehousebox.isSelected()){
            log.info("Warehouse rights enabled");
            historytab.setDisable(true);
            purchasetab.setDisable(true);
            stocktab.setDisable(false);
        }
        if (cashierbox.isSelected()){
            log.info("Cashier rights enabled");
            purchasetab.setDisable(false);
            stocktab.setDisable(true);
            historytab.setDisable(true);
        }
    }
}
