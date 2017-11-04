package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.ui.controllers.*;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

/**
 * Graphical user interface of the sales system.
 */
public class SalesSystemUI extends Application {

    private static final Logger log = LogManager.getLogger(SalesSystemUI.class);

    private final SalesSystemDAO dao;
    private final ShoppingCart shoppingCart;
    BorderPane borderPane = new BorderPane();
    private ObservableList<Tab> inizal_tabs;
    private ObservableList<Tab> cashier_tabs;
    private ObservableList<Tab> warehouse_tabs;

    public SalesSystemUI() {
        dao = new InMemorySalesSystemDAO();
        shoppingCart = new ShoppingCart(dao);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SalesSystemUI test = new SalesSystemUI();

        log.info("javafx version: " + System.getProperty("javafx.runtime.version"));

        Tab purchaseTab = new Tab();
        purchaseTab.setText("Point-of-sale");
        purchaseTab.setClosable(false);
        purchaseTab.setContent(loadControls("PurchaseTab.fxml", new PurchaseController(dao, shoppingCart)));

        Tab stockTab = new Tab();
        stockTab.setText("Warehouse");
        stockTab.setClosable(false);
        stockTab.setContent(loadControls("StockTab.fxml", new StockController(dao)));

        Tab historyTab = new Tab();
        historyTab.setText("History");
        historyTab.setClosable(false);
        historyTab.setContent(loadControls("HistoryTab.fxml", new HistoryController(dao)));

        Tab teamTab = new Tab();
        teamTab.setText("Team");
        teamTab.setClosable(false);
        teamTab.setContent(loadControls("TeamTab.fxml",  new TeamController()));

        Tab userTab = new Tab();
        userTab.setText("User");
        userTab.setClosable(false);
        userTab.setContent(loadControls("UserTab.fxml",new UserController(dao)));


        //assigining proper tabs
        ObservableList<Tab> inizal_tabs = FXCollections.observableArrayList();
        inizal_tabs.addAll(userTab,purchaseTab,stockTab,historyTab,teamTab);
        this.inizal_tabs = inizal_tabs;
        ObservableList<Tab> cashier_tabs = FXCollections.observableArrayList();
        cashier_tabs.addAll(userTab,purchaseTab,teamTab);
        this.cashier_tabs = cashier_tabs;
        ObservableList<Tab> warehouse_tabs = FXCollections.observableArrayList();
        warehouse_tabs.addAll(userTab,historyTab,teamTab);
        this.warehouse_tabs = warehouse_tabs;


        Group root = new Group();
        Scene scene = new Scene(root, 600, 500, Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("DefaultTheme.css").toExternalForm());


        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

        borderPane.setCenter(dao.init_Tabs(userTab,purchaseTab, stockTab, historyTab, teamTab));
        root.getChildren().add(borderPane);
        primaryStage.setTitle("Sales system");
        primaryStage.setScene(scene);
        primaryStage.show();

        log.info("Salesystem GUI started");

    }

    private Node loadControls(String fxml, Initializable controller) throws IOException {
        URL resource = getClass().getResource(fxml);
        if (resource == null)
            throw new IllegalArgumentException(fxml + " not found");

        FXMLLoader fxmlLoader = new FXMLLoader(resource);
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }
    public void refrech (){
        borderPane.setCenter(dao.getTabs());
    }
    public ObservableList<Tab> getInizal_tabs(){
        return inizal_tabs;
    }
    public ObservableList<Tab> getCashier_tabs(){
        return cashier_tabs;
    }
    public ObservableList<Tab> getWarehouse_tabs(){
        return warehouse_tabs;
    }
}


