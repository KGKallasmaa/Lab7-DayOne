/*
This project was made turing the Software Engineering course at Tartu University
Authors: Fred Kasemaa, Villem Laimre and Karl-Gustav Kallasmaa

 */


package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.ui.controllers.*;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.application.Application;
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
    private final BorderPane borderPane = new BorderPane();
    public SalesSystemUI() {
      //  dao = new InMemorySalesSystemDAO();
        dao = new HibernateSalesSystemDAO();
        System.out.println("Testing DAO"+dao.findStockItems());
        shoppingCart = new ShoppingCart(dao);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        //SalesSystemUI test = new SalesSystemUI();
        log.info("javafx version: " + System.getProperty("javafx.runtime.version"));

        //Scene
        Group root = new Group();
        Scene scene = new Scene(root, 600, 500, Color.WHITE);
        scene.getStylesheets().add(getClass().getResource("DefaultTheme.css").toExternalForm());


        double scene_width = scene.getWidth();
        //Tabs
        Tab purchaseTab = new Tab();
        purchaseTab.setText("Point-of-sale");
        purchaseTab.setClosable(false);
        purchaseTab.setContent(loadControls("PurchaseTab.fxml", new PurchaseController(dao, shoppingCart,scene_width)));
        Tab stockTab = new Tab();
        stockTab.setText("Warehouse");
        stockTab.setClosable(false);
        stockTab.setContent(loadControls("StockTab.fxml", new StockController(dao,scene_width)));
        Tab historyTab = new Tab();
        historyTab.setText("History");
        historyTab.setClosable(false);
        historyTab.setContent(loadControls("HistoryTab.fxml", new HistoryController(dao,scene_width)));
        Tab teamTab = new Tab();
        teamTab.setText("Team");
        teamTab.setClosable(false);
        teamTab.setContent(loadControls("TeamTab.fxml",  new TeamController()));
        Tab statTab = new Tab();
        statTab.setText("Statistics");
        statTab.setClosable(false);
        statTab.setContent(loadControls("StatTab.fxml",  new StatController(dao)));
        Tab userTab = new Tab();
        userTab.setText("User");
        userTab.setClosable(false);
        userTab.setContent(loadControls("UserTab.fxml",new UserController(dao,userTab,stockTab,purchaseTab,historyTab,teamTab,statTab)));


        borderPane.setCenter(new TabPane(userTab,purchaseTab,stockTab,historyTab,teamTab,statTab));

        borderPane.prefHeightProperty().bind(scene.heightProperty());
        borderPane.prefWidthProperty().bind(scene.widthProperty());

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
}


