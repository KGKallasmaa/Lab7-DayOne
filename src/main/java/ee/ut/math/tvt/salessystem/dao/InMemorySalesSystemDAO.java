package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.collections.ObservableList;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;



public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private List<StockItem> stockItemList;
    private HashMap<Long,List<SoldItem>> soldItemMap;

    public InMemorySalesSystemDAO() {
        this.soldItemMap = findAllOrders();

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
        this.stockItemList = stockitems;
    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;

    }
    @Override
    public HashMap<Long,List<SoldItem>> findAllOrders(){
        HashMap<Long,List<SoldItem>> orders = new HashMap<>();
        //Random Orders
        //Order 1
        StockItem stockitem_1_0= new StockItem(1L,"Test 1","Order 1",34,120);
        StockItem stockitem_1_1 = new StockItem(2L,"Test 2","Order 1",37,19);
        StockItem stockitem_1_2 = new StockItem(3L,"Test 3","Order 1",30,12);
        //Order 2
        StockItem stockitem_2_0 = new StockItem(4L,"Test 5","Order 2",35,15);
        StockItem stockitem_2_1 = new StockItem(5L,"Test 6","Order 2",39,18);
        StockItem stockitem_2_2 = new StockItem(6L,"Test 7","Order 2",100,19);
        //Order 3
        StockItem stockitem_3_0 = new StockItem(7L,"Test 8","Order 3",16,14);
        StockItem stockitem_3_1 = new StockItem(8L,"Test 9","Order 3",58,130);
        StockItem stockitem_3_2 = new StockItem(9L,"Test 10","Order 3",3,120);

        //Grouping orders
        Date date = new Date();
        Long ms_perday = 86400000L;
        Long date1 = Long.parseLong("1506609589600");
        List<SoldItem> order_1 = new ArrayList<>();
        order_1.add(new SoldItem(stockitem_1_0,stockitem_1_0.getQuantity()));
        order_1.add(new SoldItem(stockitem_1_1,stockitem_1_1.getQuantity()));
        order_1.add(new SoldItem(stockitem_1_2,stockitem_1_2.getQuantity()));
        orders.put(date1,order_1);
        Long date2 = Long.parseLong("1507224489601");
        List<SoldItem> order_2 = new ArrayList<>();
        order_2.add(new SoldItem(stockitem_2_0,20));
        order_2.add(new SoldItem(stockitem_2_1,10));
        order_2.add(new SoldItem(stockitem_2_2,33));
        orders.put(date2,order_2);
        Long date3 = Long.parseLong("1503224489601");
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
        System.out.println("Item "+stockItem.getName()+" is being processed.");
        // check if exists and add quantity if nessecary

        //could get slow with large databases
        for(StockItem oldStockItem : stockItemList){
            if (oldStockItem.getId() == stockItem.getId() && oldStockItem.getName().equals(stockItem.getName())){
                int oldQuantity = oldStockItem.getQuantity();
                oldStockItem.setQuantity(oldQuantity + stockItem.getQuantity());
                System.out.println("Quantity of " + oldStockItem.getName() + " was increased from " + oldQuantity + " to " + oldStockItem.getQuantity());
                //new price
                double oldSum = oldQuantity*oldStockItem.getPrice();
                double newSum = stockItem.getQuantity()*stockItem.getPrice();
                oldStockItem.setPrice(Math.round(((oldSum + newSum) / (oldQuantity + stockItem.getQuantity()))*100 / 100));
                return;
            }
        }
        stockItemList.add(stockItem);
    }
    @Override public void removeStockItem(StockItem stockItem) {
        if(stockItemList.contains(stockItem)){
            stockItemList.remove(stockItem);
        }
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
