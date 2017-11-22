package ee.ut.math.tvt.salessystem.logic

import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO
import ee.ut.math.tvt.salessystem.dataobjects.StockItem

class ShoppingCartTest extends GroovyTestCase {
    HibernateSalesSystemDAO dao= new HibernateSalesSystemDAO();
  //  ShoppingCart cart = new ShoppingCartTest(dao);

    void testAddItem() {
        ShoppingCart cart = new ShoppingCartTest(dao);
        StockItem  new_item = StockItem(1L,"Test1234","asd",60,4,60*4);
        final int current_nr_stockitems = cart.getAll();
        cart.addItem(new_item,10);
        boolean same_lenght = (cart.getAll().size() +1)== current_nr_stockitems+1;
        assertEquals(same_lenght,true);
        StockItem  new_item2 = StockItem(1L,"Test1234","asd",60,4,60*4);
        current_nr_stockitems = cart.getAll();
        cart.addItem(new_item,10);
        same_lenght = (cart.getAll().size())== current_nr_stockitems;
        assertEquals(same_lenght,true);
        new_item2 = StockItem(1L,"Test12345","asd",-60,4,60*4);
        current_nr_stockitems = cart.getAll();
        cart.addItem(new_item,10);
        same_lenght = (cart.getAll().size())== current_nr_stockitems;
        assertEquals(same_lenght,true);
        new_item2 = StockItem(1L,"Test12345","asd",-60,-4,60*4);
        current_nr_stockitems = cart.getAll();
        cart.addItem(new_item,10);
        same_lenght = (cart.getAll().size())== current_nr_stockitems;
        assertEquals(same_lenght,true);
        new_item2 = StockItem(1L,"Test12345","asd",60,-4,60*4);
        current_nr_stockitems = cart.getAll();
        cart.addItem(new_item,10)
        same_lenght = (cart.getAll().size())== current_nr_stockitems;
        assertEquals(same_lenght,true);
    }

    void testCancelCurrentPurchase() {
        ShoppingCart cart = new ShoppingCartTest(dao);
        StockItem  new_item = StockItem(1L,"Test1234","asd",60,4,60*4);
        final int current_nr_stockitems = cart.getAll();
        cart.addItem(new_item,10);
        cart.cancelCurrentPurchase();
        assertEquals(cart.getAll().size(),0);

    }

    void testSubmitCurrentPurchase() {
    }
}
