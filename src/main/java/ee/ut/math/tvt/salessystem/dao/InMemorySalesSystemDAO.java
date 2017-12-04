package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;



public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private List<StockItem> stockItemList;
    private HashMap<Long,List<SoldItem>> soldItemMap;
    //private static final Logger log = LogManager.getLogger(InMemorySalesSystemDAO.class);

    public InMemorySalesSystemDAO() {
        List<StockItem> stockitems = new ArrayList<>();
        //Random example stockitems

        StockItem stockitem_1= new StockItem(1L,"Test 1","test1",34.0,120);
        StockItem stockitem_2 = new StockItem(2L,"Test 2","test2",37.0,190);
        StockItem stockitem_3 = new StockItem(3L,"Test 3","test3",35.0,12);
        StockItem stockitem_4= new StockItem(1L,"Test 4","test1",34.0,120);
        StockItem stockitem_5 = new StockItem(2L,"Test 5","test2",35.0,9);
        StockItem stockitem_6 = new StockItem(3L,"Test 6","test3",3.0,12);
        StockItem stockitem_7= new StockItem(1L,"Test 7","test1",340.0,120);
        StockItem stockitem_8 = new StockItem(2L,"Test 8","test2",7.0,19);
        StockItem stockitem_9 = new StockItem(3L,"Test 9","test3",32.0,124);
        StockItem stockitem_10 = new StockItem(3L,"Test 10","test3",39.0,142);
        //Adding random stockitems to stocitems list
        stockitems.add(stockitem_1);
        stockitems.add(stockitem_2);
        stockitems.add(stockitem_3);
        stockitems.add(stockitem_4);
        stockitems.add(stockitem_5);
        stockitems.add(stockitem_6);
        stockitems.add(stockitem_7);
        stockitems.add(stockitem_8);
        stockitems.add(stockitem_9);
        stockitems.add(stockitem_10);
        this.stockItemList = stockitems;

        // create a few example orders
        HashMap<Long,List<SoldItem>> orders = new HashMap<>();
        //Random Orders
        //Order 1
        StockItem stockitem_1_0= new StockItem(1L,"Test 1","Order 1",34.0,120);
        StockItem stockitem_1_1 = new StockItem(2L,"Test 2","Order 1",37.0,19);
        StockItem stockitem_1_2 = new StockItem(3L,"Test 3","Order 1",30.0,12);
        //Order 2
        StockItem stockitem_2_0 = new StockItem(4L,"Test 5","Order 2",35.0,15);
        StockItem stockitem_2_1 = new StockItem(5L,"Test 6","Order 2",39.0,18);
        StockItem stockitem_2_2 = new StockItem(6L,"Test 7","Order 2",100.5,19);
        //Order 3
        StockItem stockitem_3_0 = new StockItem(7L,"Test 8","Order 3",16.3,14);
        StockItem stockitem_3_1 = new StockItem(8L,"Test 9","Order 3",58.4,130);
        StockItem stockitem_3_2 = new StockItem(9L,"Test 10","Order 3",3.2,120);


        //Grouping orders
    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
    }
    @Override
    public HashMap<Long,List<SoldItem>> findAllOrders(){
        return soldItemMap;
    }
    @Override
    public StockItem findStockItem(long id) {
        for (StockItem item : stockItemList) {
            if (item.getId() == id)
                return item;
        }
        return null;
    }
    @Override
    public StockItem findStockItemName(String name){
        for (StockItem item : stockItemList) {
            if (item.getName() == name){
                return item;
            }
        }
        return null;
    }
    @Override
    public void saveSoldItem(SoldItem item, boolean started) {
        if (soldItemMap.containsKey(item.getTime())) {
            List<SoldItem> orders = soldItemMap.get(item.getTime());
            orders.add(item);
            soldItemMap.put(item.getTime(), orders);
        } else {
            List<SoldItem> orders = new ArrayList<>();
            orders.add(item);
            soldItemMap.put(item.getTime(), orders);
        }
    }

    @Override
    public List<SoldItem> findSoldItems(){
        return null;
    }
    @Override
    public HashMap<StockItem, Integer> stockitem_maxquantity(){
        return null;
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        // check if exists and add quantity if nessecary
        // could get slow with large databases
            for(StockItem oldStockItem : stockItemList){
                if (oldStockItem.getId() == stockItem.getId() && oldStockItem.getName().equals(stockItem.getName())){
                    int oldQuantity = oldStockItem.getQuantity();
                    oldStockItem.setQuantity(oldQuantity + stockItem.getQuantity());
                    //new price
                    double oldSum = oldQuantity*oldStockItem.getPrice();
                    double newSum = stockItem.getQuantity()*stockItem.getPrice();
                    oldStockItem.setPrice(Math.round(((oldSum + newSum) / (oldQuantity + stockItem.getQuantity()))*100 / 100));
                    return;
                }
            }
            stockItemList.add(stockItem);
        }
    @Override public void removeStockItem(StockItem stockItem,boolean started) {

        StockItem removable_obj = findStockItem(stockItem.getId());
        if (removable_obj.getQuantity() < stockItem.getQuantity()) {
            throw new IllegalArgumentException();
        }
        removable_obj.setQuantity(removable_obj.getQuantity() - stockItem.getQuantity());
        if (removable_obj.getQuantity() == 0) {
            stockItemList.remove(removable_obj);
        }
    }
    @Override
    public void beginTransaction() {
    }
    @Override
    public List<SoldItem> findOrderByDate(Date date){
        return null;
    }
    @Override
    public void rollbackTransaction() {
    }
    @Override
    public void commitTransaction() {
    }
}
