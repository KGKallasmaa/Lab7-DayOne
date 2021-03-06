package ee.ut.math.tvt.salessystem.ui.controllers;


import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserController  implements Initializable {

    private final SalesSystemDAO dao;
    private static final Logger log = LogManager.getLogger(UserController.class);
    @FXML private javafx.scene.control.CheckBox cashierbox;
    @FXML private javafx.scene.control.CheckBox warehousebox;
    @FXML private javafx.scene.control.CheckBox adminbox;
    @FXML private final javafx.scene.control.Tab stocktab;
    @FXML private final javafx.scene.control.Tab usertab;
    @FXML private final javafx.scene.control.Tab purchasetab;
    @FXML private final javafx.scene.control.Tab historytab;
    @FXML private final javafx.scene.control.Tab teamtab;
    @FXML private final javafx.scene.control.Tab stattab;

    public UserController(SalesSystemDAO dao, Tab usertab,Tab stocktab,Tab purchasetab,Tab historytab,Tab teamtab, Tab stattab) {
        this.dao = dao;
        this.usertab = usertab;
        this.stocktab = stocktab;
        this.purchasetab = purchasetab;
        this.historytab = historytab;
        this.teamtab= teamtab;
        this.stattab= stattab;
    }
    @Override public void initialize(URL location, ResourceBundle resources) {
        log.debug("User tab initialized");
        historytab.setDisable(true);
        purchasetab.setDisable(true);
        stocktab.setDisable(true);
        teamtab.setDisable(true);
        stattab.setDisable(true);
    }
    @FXML public void setCashierbox (){
        log.debug("Usertype = Cashier");
        this.cashierbox.setSelected(true);
        this.warehousebox.setSelected(false);
        this.adminbox.setSelected(false);
        selectUser();
    }
    @FXML public void setWarehousebox (){
        log.debug("Usertype = Warehouse worker");
        this.warehousebox.setSelected(true);
        this.cashierbox.setSelected(false);
        this.adminbox.setSelected(false);
        selectUser();
    }
    @FXML public void setAdminbox (){
        log.debug("Usertype = Admin");
        this.warehousebox.setSelected(false);
        this.cashierbox.setSelected(false);
        this.adminbox.setSelected(true);
        selectUser();
    }
    @FXML
    private void selectUser(){
        if (adminbox.isSelected()){
            log.info("Admin rights enabled");
            historytab.setDisable(false);
            purchasetab.setDisable(false);
            stocktab.setDisable(false);
            teamtab.setDisable(false);
            stattab.setDisable(false);
        }
        if (warehousebox.isSelected()){
            log.info("Warehouse rights enabled");
            historytab.setDisable(true);
            purchasetab.setDisable(true);
            stocktab.setDisable(false);
            teamtab.setDisable(false);
            stattab.setDisable(true);
        }
        if (cashierbox.isSelected()){
            log.info("Cashier rights enabled");
            purchasetab.setDisable(false);
            stocktab.setDisable(true);
            historytab.setDisable(true);
            teamtab.setDisable(false);
            stattab.setDisable(true);
        }
    }
}
