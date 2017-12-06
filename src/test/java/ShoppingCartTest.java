import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShoppingCartTest {
    private static SalesSystemDAO dao;

    @BeforeClass public static void setup(){
        dao = new InMemorySalesSystemDAO();
    }
    @Test public void AddingExistingItem(){
        ShoppingCart cart = new ShoppingCart(dao);
        ShoppingCart cart2 = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(2), 2);
        cart.addItem(dao.findStockItem(2), 2);
        cart2.addItem(dao.findStockItem(2), 4);
        assertEquals(cart.getAll().toString(), cart2.getAll().toString());
    }
    @Test public void AddingNewItem(){
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), 1);
        assertTrue("insert failure message here", cart.getAll().size() == 1);
    }
    @Test public void AddingItemWithNegativeQuantity(){
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), -1);
        assertEquals(new ArrayList<>(), cart.getAll());
    }
    @Test public void AddingItemWithQuantityTooLarge(){
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), dao.findStockItem(1).getQuantity()+1);
        assertEquals(new ArrayList<>(), cart.getAll());
    }
    @Test public void AddingItemWithQuantitySumTooLarge(){
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), dao.findStockItem(1).getQuantity());
        cart.addItem(dao.findStockItem(1), 1);

        assertEquals(dao.findStockItem(1).getQuantity(), cart.getAll().get(0).getQuantity());
    }

    @Test public void SubmittingCurrentPurchaseDecreasesStockItemQuantity(){
        ShoppingCart cart = new ShoppingCart(dao);
        int prevQuantity = dao.findStockItem(1).getQuantity();
        cart.addItem(dao.findStockItem(1), 1);
        cart.submitCurrentPurchase();
        assertEquals(prevQuantity - 1, dao.findStockItem(1).getQuantity());
    }
    @Test public void SubmittingCurrentPurchaseBeginsAndCommitsTransaction(){

    }
    @Test public void SubmittingCurrentOrderCreatesHistoryItem(){} // we have no such thing a historyitem
    @Test public void SubmittingCurrentOrderSavesCorrectTime(){
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), 1);
        Long prevTime = System.currentTimeMillis();
        cart.submitCurrentPurchase();
        HashMap<Long, List<SoldItem>> soldorders = dao.findAllOrders();
        assertTrue(soldorders.containsKey(prevTime));
    }
    @Test public void CancellingOrder() {
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), 1);
        cart.cancelCurrentPurchase();
        assertEquals(new ArrayList<>(), cart.getAll());
    }
    @Test public void CancellingOrderQuanititesUnchanged(){
        ShoppingCart cart = new ShoppingCart(dao);
        cart.addItem(dao.findStockItem(1), 1);
        int prevQuantity = dao.findStockItem(1).getQuantity();
        cart.cancelCurrentPurchase();
        assertEquals(prevQuantity, dao.findStockItem(1).getQuantity());
    }

}
