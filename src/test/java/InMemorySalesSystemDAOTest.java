import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import org.junit.Test;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemorySalesSystemDAOTest {

    SalesSystemDAO dao = new InMemorySalesSystemDAO();

    @Test public void testAddingItemBeginsAndCommitsTransaction(){

    }
    @Test public void testAddingNewItem(){
        StockItem item = new StockItem(100L,"Tere123","tere123",130.0,120);
        dao.saveStockItem(item);
        assertEquals(dao.findStockItem(100L),item);
    }
    @Test public void testAddingExistingItem(){
        StockItem item = new StockItem(1L,"Test 1","test 1",12.0,4);
        int q = dao.findStockItem(1L).getQuantity();
        dao.saveStockItem(item);
        assertEquals(dao.findStockItem(1L).getQuantity(),q+4);
    }
    @Test public void testAddingItemWithNegativeQuantity(){
        StockItem item = new StockItem(150L,"Tere123","tere123",130.0,-12);
        dao.saveStockItem(item);
        assertNull(dao.findStockItem(150L));
    }

}