package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Long.MAX_VALUE;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final HashMap<Long,List<SoldItem>> soldItemMap;

    public InMemorySalesSystemDAO() {
        this.stockItemList = findStockItems();
        this.soldItemMap = findAllOrders();
    }

    @Override
    public List<StockItem> findStockItems() {
        List<StockItem> stockitems = new ArrayList<>();

        //Random stockitems
        StockItem stockitem_1 = new StockItem(1L,"Test1","I'm cool",340,1200);
        StockItem stockitem_2 = new StockItem(2L,"Test2","I'm cool",440,1220);
        StockItem stockitem_3 = new StockItem(3L,"Test3","I'm cool",540,1230);
        StockItem stockitem_4 = new StockItem(4L,"Test4","I'm cool",640,1240);
        StockItem stockitem_5 = new StockItem(5L,"Test5","I'm cool",740,1250);
        StockItem stockitem_6 = new StockItem(6L,"Test6","I'm cool",840,1260);
        StockItem stockitem_7 = new StockItem(7L,"Test7","I'm cool",940,1270);
        StockItem stockitem_8 = new StockItem(8L,"Test8","I'm cool",240,1280);
        StockItem stockitem_9 = new StockItem(9L,"Test9","I'm cool",140,1290);
        StockItem stockitem_10 = new StockItem(10L,"Test10","I'm cool",40,1300);

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

        return stockitems;

    }
    @Override
    public HashMap<Long,List<SoldItem>> findAllOrders(){
        HashMap<Long,List<SoldItem>> orders = new HashMap<>();
        //Random Orders
        //Order 1
        StockItem stockitem_1_0= new StockItem(1L,"Test 1","Order 1",340,120);
        StockItem stockitem_1_1 = new StockItem(2L,"Test 1","Order 1",340,120);
        StockItem stockitem_1_2 = new StockItem(3L,"Test 1","Order 1",340,120);
        //Order 2
        StockItem stockitem_2_0 = new StockItem(4L,"Test 2","Order 2",350,150);
        StockItem stockitem_2_1 = new StockItem(5L,"Test 2","Order 2",350,180);
        StockItem stockitem_2_2 = new StockItem(6L,"Test 2","Order 2",350,190);
        //Order 3
        StockItem stockitem_3_0 = new StockItem(7L,"Test 3","Order 3",360,140);
        StockItem stockitem_3_1 = new StockItem(8L,"Test 3","Order 3",380,130);
        StockItem stockitem_3_2 = new StockItem(9L,"Test 3","Order 3",390,120);

        //Grouping orders
        Date date = new Date();
        Long ms_perday = 86400000L;
        Long date1 = date.getTime();
        List<SoldItem> order_1 = new ArrayList<>();
        order_1.add(new SoldItem(stockitem_1_0,stockitem_1_0.getQuantity()));
        order_1.add(new SoldItem(stockitem_1_1,stockitem_1_1.getQuantity()));
        order_1.add(new SoldItem(stockitem_1_2,stockitem_1_2.getQuantity()));
        orders.put(date1,order_1);
        Long date2 = date1-7*ms_perday;
        List<SoldItem> order_2 = new ArrayList<>();
        order_2.add(new SoldItem(stockitem_2_0,20));
        order_2.add(new SoldItem(stockitem_2_1,10));
        order_2.add(new SoldItem(stockitem_2_2,33));
        orders.put(date2,order_2);
        Long date3 = date2-30*ms_perday;
        List<SoldItem> order_3 = new ArrayList<>();
        order_3.add(new SoldItem(stockitem_3_0,90));
        order_3.add(new SoldItem(stockitem_3_1,14));
        order_3.add(new SoldItem(stockitem_3_2,23));
        orders.put(date3,order_2);

        return orders;
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
    public void saveSoldItem(Long time, SoldItem item) {
        if (soldItemMap.containsKey(time)) {

            List<SoldItem> orders = soldItemMap.get(time);
            orders.add(item);
            soldItemMap.put(time, orders);
        } else {
            List<SoldItem> orders = new ArrayList<>();
            orders.add(item);
            soldItemMap.put(time, orders);
        }
    }

    @Override
    public void saveStockItem(StockItem stockItem) {
        stockItemList.add(stockItem);
    }

    @Override
    public void beginTransaction() {
    }

    @Override
    public void rollbackTransaction() {
    }

    @Override
    public void commitTransaction() {
    }



}
