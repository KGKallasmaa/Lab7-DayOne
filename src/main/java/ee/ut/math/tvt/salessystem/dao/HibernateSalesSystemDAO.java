package ee.ut.math.tvt.salessystem.dao;

import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
        em.merge(stockItem);
        em.persist(stockItem);
    }
    @Override
    public void saveSoldItem(Long time, SoldItem item){
        em.merge(item);
        em.persist(item);
    }
    @Override
         //   em.remove(stockItem);
    public void removeStockItem(StockItem stockItem){
        if(findStockItems().contains(stockItem)){
           em.remove(stockItem);
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
}