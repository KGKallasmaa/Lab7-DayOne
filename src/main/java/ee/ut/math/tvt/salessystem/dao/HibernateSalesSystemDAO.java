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
        //TODO
    }
    @Override
    public void removeStockItem(StockItem stockItem){
        em.remove(stockItem);
    }

    @Override
    public HashMap<Long,List<SoldItem>> findAllOrders(){
        //TODO
        return null;
    }
   @Override
    public StockItem findStockItem(long id){
       em.find(StockItem.class, id);
       return null;
    }
    @Override
    public StockItem findStockItemName(String name){
        //TODO
        int id = em.createQuery("SELECT stockitem FROM StockItem stockitem WHERE stockitem.name = :name").setParameter(name, name).getFirstResult();
        return this.findStockItem(id);
    }
    @Override

    public List<StockItem> findStockItems(){
       return  em.createQuery("from StockItem",StockItem.class).getResultList();
     //  return null;
    }

}