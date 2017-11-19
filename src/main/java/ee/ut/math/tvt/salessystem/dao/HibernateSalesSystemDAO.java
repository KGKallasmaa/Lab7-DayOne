package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class HibernateSalesSystemDAO implements SalesSystemDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;
  //  private static final Logger log = LogManager.getLogger(HibernateSalesSystemDAO.class);

    public HibernateSalesSystemDAO() {
        // if you get ConnectException/JDBCConnectionException then you
        // probably forgot to start the database before starting the application
        emf = Persistence.createEntityManagerFactory("pos");
        em = emf.createEntityManager();
    }

    // TODO implement missing methods


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
    @Override
    public void saveStockItem(StockItem stockItem) {
        try{
            StockItem existingItem = findStockItem(stockItem.getId());
            beginTransaction();
            existingItem.setQuantity(existingItem.getQuantity()+stockItem.getQuantity());
            if (existingItem.getQuantity() <= 0){
                em.remove(existingItem);
            }
            commitTransaction();
            return;
        }catch (Exception e){
            rollbackTransaction();
            e.printStackTrace();
        }
        beginTransaction();
        em.merge(stockItem);
        commitTransaction();
    }
    @Override
    public void saveSoldItem(Long time, SoldItem item){
        beginTransaction();
        //em.merge(saveSoldItem());
        commitTransaction();
    }
    @Override
    public void removeStockItem(StockItem stockItem){

        StockItem tempSI = this.findStockItem(stockItem.getId());
        int newQuantity = tempSI.getQuantity() - stockItem.getQuantity();
        em.merge(tempSI);
        if (newQuantity>0){
            tempSI.setQuantity(newQuantity);
        } else{
            //em.remove(tempSI);
            em.detach(tempSI);
        }
    }

    @Override
    public HashMap<Long,List<SoldItem>> findAllOrders(){
        //TODO
        return null;
    }
   @Override
    public StockItem findStockItem(long id){
       List<StockItem> stockItemsWithId = em.createQuery("SELECT stockitem FROM StockItem stockitem WHERE stockitem.name = :name")
               .setParameter("stockitem_id", id)
               .getResultList();
       return stockItemsWithId.get(0);
    }
    @Override
    public StockItem findStockItemName(String name){
        //TODO
        List<StockItem> stockItemsWithName = em.createQuery("SELECT stockitem FROM StockItem stockitem WHERE stockitem.name = :name")
                .setParameter("name", name)
                .getResultList();
        return stockItemsWithName.get(0);
    }
    @Override
    public List<StockItem> findStockItems(){
       return  em.createQuery("from StockItem",StockItem.class).getResultList();
     //  return null;
    }
    public List<SoldItem> findOrderByDate(Date date){
        return em.createQuery("SELECT solditem FROM SoldItem solditem WHERE solditem.date = :date")
                .setParameter("date", date)
                .getResultList();
    }
}