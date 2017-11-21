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
    public ShoppingCart(ShoppingCart oldCart){
        this.items = oldCart.items;
        this.dao = oldCart.dao;
        this.item_max = oldCart.item_max;
    }
    public void clear(){
        items.clear();
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(StockItem newItem, Integer quantity) {
        StockItem newstockitem = new StockItem(newItem); // copy constructor
        boolean shoppincartHasItem = false;
        for(StockItem stockitem : items.keySet()) {
            if (stockitem.getId() == newstockitem.getId()) {// items.containsKey(newstockitem)
                shoppincartHasItem = true;
                int current_quantity = items.get(stockitem);
                if (quantity > 0) {
                    int max_q = item_max.get(newItem);
                    int new_quantity = current_quantity + quantity;
                    if (new_quantity > max_q) {
                        new_quantity = max_q;
                    }
                    stockitem.setQuantity(new_quantity);
                    stockitem.setSum((double) new_quantity * newstockitem.getPrice());
                    items.put(stockitem, new_quantity);
                }
            }
        }
        if (!shoppincartHasItem){
            newstockitem.setQuantity(quantity);
            newstockitem.setSum((double) newItem.getQuantity() * newstockitem.getPrice());
            items.put(newstockitem, quantity);
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
        //dao.beginTransaction();
        final Long time = System.currentTimeMillis();
        final Date date = new Date(time);
        List<SoldItem> current_solditems = dao.findSoldItems();
        try {
            int i = 0;
            for (StockItem item : items.keySet()) { //TODO: broken ?
                Long id = Long.valueOf(current_solditems.size()+1);
                SoldItem new_solditem = new SoldItem(id,time, item.getId(), items.get(item));
                i++;
                dao.saveSoldItem(new_solditem,false);
                dao.removeStockItem(item,false);
            }
            //dao.commitTransaction();
            clear();
            //this.items = new HashMap<>();

        } catch (Exception e) {
            System.out.println("Something went wrong with subbmiting the purcahse "+e.getMessage());
            e.printStackTrace();
            //dao.rollbackTransaction();
        }
    }
}
