package groovy.util;

import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.junit.Test;



import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemorySalesSystemDAOTest {

    SalesSystemDAO dao = new InMemorySalesSystemDAO();
    @Test
    public void testAddingItemBeginsAndCommitsTransaction(){
        //TODO- this method needs to be implemented
    }
    @Test
    void testAddingNewItem(){
        StockItem item = new StockItem(100L,"Tere123","tere123",130.0,120);
        dao.saveStockItem(item);
        assertEquals(dao.findStockItem(100L),item);
    }
    @Test
    void testAddingExistingItem(){
        StockItem item = new StockItem(1L,"Tere1","tere1",12.0,4);
        dao.saveStockItem(item);
        int q = dao.findStockItem(1L).getQuantity();
        assertEquals(dao.findStockItem(1L).getQuantity(),q+4);
    }
    @Test
    void testAddingItemWithNegativeQuantity(){
        StockItem item = new StockItem(150L,"Tere123","tere123",130.0,-12);
        dao.saveStockItem(item);
        assertNull(dao.findStockItem(150L));
    }

}