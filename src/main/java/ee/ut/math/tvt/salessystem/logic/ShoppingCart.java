package ee.ut.math.tvt.salessystem.logic;

import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javafx.scene.control.DatePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

public class ShoppingCart {

    private final SalesSystemDAO dao;
    private final List<SoldItem> items = new ArrayList<>();

    public ShoppingCart(SalesSystemDAO dao) {
        this.dao = dao;
    }

    /**
     * Add new SoldItem to table.
     */
    public void addItem(SoldItem newItem) {
        // TODO verify that warehouse items' quantity remains at least zero or throw an exception
        boolean isInList = false;
        for (SoldItem item : items){
            if(newItem.getId().equals(item.getId())){
                if(newItem.getQuantity() + item.getQuantity() <= 0){
                    items.remove(item);
                }
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                item.setSum(item.getQuantity() * item.getPrice());
                isInList = true;
                break;
            }
        }
        if (!isInList && newItem.getQuantity()>0) {
            items.add(newItem);
        }
        //log.debug("Added " + item.getName() + " quantity of " + item.getQuantity());
    }

    public List<SoldItem> getAll() {
        return items;
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
        try {
            for (SoldItem item : items) {
                Date date = new Date();
                dao.saveSoldItem(date.getTime(),item);
            }
            dao.commitTransaction();
            items.clear();
        } catch (Exception e) {
            dao.rollbackTransaction();
            throw e;
        }
    }
}
