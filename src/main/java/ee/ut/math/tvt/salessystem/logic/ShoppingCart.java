package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import java.util.*;

public class ShoppingCart {

    private final SalesSystemDAO dao;

    private  HashMap<StockItem, Integer> items = new HashMap<>();
    private  HashMap<StockItem,Integer> item_max = new HashMap<>();
    public ShoppingCart(SalesSystemDAO dao) {
        this.dao = dao;
        this.item_max = dao.stockitem_maxquantity();
    }
    public void clear(){
        items.clear();
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(StockItem newItem, Integer quantity) {
        if (items.containsKey(newItem)) {
            int current_quantity = items.get(newItem);
            if(quantity > 0){
                int max_q = item_max.get(newItem);
                int new_quantity = current_quantity + quantity;
                if(new_quantity > max_q){
                    new_quantity = max_q;
                }
                newItem.setQuantity(new_quantity);
                newItem.setSum((double)new_quantity*newItem.getPrice());
                items.put(newItem, new_quantity);
            }
        } else {
            newItem.setQuantity(quantity);
            newItem.setSum((double)newItem.getQuantity()*newItem.getPrice());
            items.put(newItem, quantity);
        }

    }

    public List<StockItem> getAll() {
        ArrayList new_list = new ArrayList();
        new_list.addAll(items.keySet());
        return new_list;
    }

    public void cancelCurrentPurchase() {
        items.clear();
    }

    public void submitCurrentPurchase() {
        // TODO decrease quantities of the warehouse stock

        // note the use of transactions. InMemorySalesSystemDAO ignores transactions
        // but when you start using hibernate in lab5, then it will become relevant.
        // what is a transaction? https://stackoverflow.com/q/974596
        dao.beginTransaction();
        final Long time = System.currentTimeMillis();
        final Date date = new Date(time);
        List<SoldItem> current_solditems = dao.findSoldItems();
        try {
            int i = 0;
            for (StockItem item : items.keySet()) { //TODO: fix this
                Long id = Long.valueOf(current_solditems.size()+1);
                SoldItem new_solditem = new SoldItem(id,time, item.getId(), items.get(item));
                i++;
                dao.saveSoldItem(new_solditem,true);
                dao.removeStockItem(item,true);
            }
            dao.commitTransaction();
            items.clear();

        } catch (Exception e) {
            System.out.println("Something went wrong with subbmiting the purcahse "+e.getMessage());
            dao.rollbackTransaction();
        }
    }
}
