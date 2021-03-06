package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import javax.persistence.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HibernateSalesSystemDAO implements SalesSystemDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;
    private int a;
    //  private static final Logger log = LogManager.getLogManager(HibernateSalesSystemDAO.class);

    public HibernateSalesSystemDAO() {
        // if you get ConnectException/JDBCConnectionException then you
        // probably forgot to start the database before starting the application
        emf = Persistence.createEntityManagerFactory("pos");
        em = emf.createEntityManager();
    }

    public void close() {
        em.close();
        emf.close();
    }

    @Override
    public void beginTransaction() {
        em.getTransaction().begin();
    }

    @Override
    public void rollbackTransaction() {
        em.getTransaction().rollback();
    }

    @Override
    public void commitTransaction() {
        em.getTransaction().commit();
    }

    @Override public void saveStockItem(StockItem stockItem) {
        try {
            double p1 = 0;
            int existing_q = 0;
            StockItem existingItem = findStockItem(stockItem.getId());
            if (existingItem != null) {
                p1 += existingItem.getQuantity() * existingItem.getPrice();
                existing_q += existingItem.getQuantity();
            }

            double p2 = stockItem.getQuantity() * stockItem.getPrice();
            beginTransaction();
            stockItem.setQuantity(existing_q + stockItem.getQuantity());
            double new_price = Math.round((p1 + p2) / stockItem.getQuantity());
            stockItem.setPrice(new_price);
            em.merge(stockItem);
            commitTransaction();
        } catch (IndexOutOfBoundsException e) {
            beginTransaction();
            em.merge(stockItem);
            commitTransaction();
            //  e.printStackTrace();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, "Please enter a number to Barcode," +
                            " Price and Quantity fields.", "Error",
                    JOptionPane.ERROR_MESSAGE);

        } catch (Exception e) {
            //     JOptionPane.showMessageDialog(null, "An item with that name already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            beginTransaction();
            em.merge(stockItem);
            commitTransaction();
        }
    }

    @Override public void saveSoldItem(SoldItem item, boolean started) {
        beginTransaction();
        em.merge(item);
        commitTransaction();
    }

    @Override public void removeStockItem(StockItem stockItem, boolean started) {
        if (!started) {
            beginTransaction();
        }
        StockItem tempSI = this.findStockItem(stockItem.getId());
        int newQuantity = tempSI.getQuantity() - stockItem.getQuantity();
        em.merge(tempSI);
        if (newQuantity > 0) {
            tempSI.setQuantity(newQuantity);
        } else {
            tempSI.setQuantity(0);
            //    em.remove(tempSI);
        }
        if (!started) {
            commitTransaction();
        }
    }

    @Override public HashMap<Long, List<SoldItem>> findAllOrders() {
        List<SoldItem> soldItems = em.createQuery("from SoldItem", SoldItem.class).getResultList();
        HashMap<Long, List<SoldItem>> orders = new HashMap<>();
        for (SoldItem el : soldItems) {
            if (orders.containsKey(el.getTime())) {
                List<SoldItem> current_list = orders.get(el.getTime());
                current_list.add(el);
                orders.put(el.getTime(), current_list);
            } else {
                List<SoldItem> new_list = new ArrayList<>();
                new_list.add(el);
                orders.put(el.getTime(), new_list);
            }
        }
        return orders;
    }

    @Override public StockItem findStockItem(long id) {
        List<StockItem> stockItemsWithId = em.createQuery("SELECT stockitem FROM StockItem stockitem WHERE stockitem.stockitem_id = :id")
                .setParameter("id", id)
                .getResultList();
        if (stockItemsWithId.size() > 0) {
            return stockItemsWithId.get(0);
        } else {
            return null;
        }
    }

    public SoldItem findSoldItem(long id) {
        List<SoldItem> soldItemsWithId = em.createQuery("SELECT solditem FROM SoldItem solditem WHERE solditem.solditem_id = :id")
                .setParameter("id", id)
                .getResultList();
        return soldItemsWithId.get(0);
    }

    @Override public StockItem findStockItemName(String name) {
        try {
            List<StockItem> stockItemsWithName = em.createQuery("SELECT stockitem FROM StockItem stockitem WHERE stockitem.name = :name")
                    .setParameter("name", name)
                    .getResultList();
            if (stockItemsWithName.size() >= 1) {
                return stockItemsWithName.get(0);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override public List<StockItem> findStockItems(){
       return  em.createQuery("from StockItem",StockItem.class).getResultList();
    }

    @Override public List<SoldItem> findOrderByDate(Date date){
        return em.createQuery("SELECT * FROM SoldItem solditem WHERE solditem.time = :time")
                .setParameter("time", date.getTime())
                .getResultList();
    }

    @Override public List<SoldItem> findSoldItems(){
        return  em.createQuery("from SoldItem",SoldItem.class).getResultList();
        //  return null;
    }

    @Override public HashMap<StockItem,Integer> stockitem_maxquantity(){
        HashMap<StockItem,Integer> item_max = new HashMap<>();
        for (StockItem el : findStockItems()){
            item_max.put(el,el.getQuantity());
        }
        return item_max;
    }

    @Override public void setA(int a) {
        this.a = a;
    }
    @Override public int getA(){return a;}
}