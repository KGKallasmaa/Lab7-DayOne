package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Long.MAX_VALUE;

public class InMemorySalesSystemDAO implements SalesSystemDAO {

    private final List<StockItem> stockItemList;
    private final HashMap<Long,List<SoldItem>> soldItemMap;

    public InMemorySalesSystemDAO() {
        List<StockItem> items = new ArrayList<StockItem>();
        items.add(new StockItem(1L, "Lays chips", "Potato chips", 11.0, 5));
        items.add(new StockItem(2L, "Chupa-chups", "Sweets", 8.0, 8));
        items.add(new StockItem(3L, "Frankfurters", "Beer sauseges", 15.0, 12));
        items.add(new StockItem(4L, "Free Beer", "Student's delight", 0.0, 100));
        items.add(new StockItem(5L, "Free Beer", "Student's delight", 0.0, 300));
        this.stockItemList = items;
        HashMap<Long,List<SoldItem>> test = new HashMap<>();
        this.soldItemMap = test;


        //Sample order
        List<StockItem> order_items = new ArrayList<>();
        order_items.add(new StockItem(1L, "Test 1", "I'm cool", 12.0, 6));
        order_items.add(new StockItem(1L, "Test 2", "I'm cooler", 12.0, 6));
        order_items.add(new StockItem(1L, "Test 3", "I'm way cooler", 13.0, 7));
        order_items.add(new StockItem(1L, "Test 4", "I'm the best", 14.0, 8));

        Date date = new Date();
        Long time = MAX_VALUE;
        List<SoldItem> sold_list = new ArrayList<SoldItem>();
        for(StockItem el : order_items){
            SoldItem item = new SoldItem(el,el.getQuantity());
            sold_list.add(item);
        }
        soldItemMap.put(time,sold_list);

    }

    @Override
    public List<StockItem> findStockItems() {
        return stockItemList;
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
    public void saveSoldItem(Long date, SoldItem item) {
        List<SoldItem> orders = soldItemMap.get(date);
        orders.add(item);
        soldItemMap.put(date,orders);
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
    @Override
    public HashMap<Long, List<SoldItem>> getSoldItemMap() {
        return soldItemMap;
    }
}
