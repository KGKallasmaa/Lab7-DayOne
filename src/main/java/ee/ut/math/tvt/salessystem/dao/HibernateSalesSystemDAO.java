package ee.ut.math.tvt.salessystem.dao;


import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class HibernateSalesSystemDAO implements SalesSystemDAO {

    private final EntityManagerFactory emf;
    private final EntityManager em;

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
    public void saveStockItem(StockItem stockItem){
        //TODO
    }
    @Override
    public void saveSoldItem(Long time, SoldItem item){
        //TODO
    }
    @Override
    public void removeStockItem(StockItem stockItem){
        //TODO
    }

    @Override
    public HashMap<Long,List<SoldItem>> findAllOrders(){
        //TODO
        return null;
    }
   @Override
    public StockItem findStockItem(long id){
        //TODO
        return null;
    }
    @Override
    public StockItem findStockItemName(String name){
        //TODO
        return null;
    }
    @Override
    public List<StockItem> findStockItems(){
        //TODO
       return  em.createQuery("from Stockitem",StockItem.class).getResultList();
     //  return null;
    }

}