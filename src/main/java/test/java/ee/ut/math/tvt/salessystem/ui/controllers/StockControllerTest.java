package ee.ut.math.tvt.salessystem.ui.controllers;

import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockControllerTest {
    HibernateSalesSystemDAO dao = new HibernateSalesSystemDAO();
    double width = 600;
    StockController stockController = new StockController(dao,width);

    @Test
    void clearAll() {
        stockController.clearAll();
        Long id = stockController.getId();
        Integer quantity = stockController.getQuantity();
        String name = stockController.getName();
        Double price = stockController.getPrice();
        String description = stockController.getDescription();
        boolean all_null = true;
        if (id != null || quantity != null ||price != null || description != null){
            all_null = false;
        }
        assertEquals(all_null, true);
    }

    @Test
    void removeButtonClicked() {

        //Item Excist

        //Origninal
        Long id = 1L;
        Integer quantity = dao.findStockItem(id).getQuantity();
        String name = dao.findStockItem(id).getName();
        Double price = dao.findStockItem(id).getPrice();
        String description = dao.findStockItem(id).getDescription();
        //New
        id = 1L;
        Integer new_quantity = 12;
        price = 12.0;
        description = "tere1";
        name = "Tere1";
        stockController.setId(id);
        stockController.setQuantity(new_quantity);
        stockController.setName(name);
        stockController.setPrice(price);
        stockController.setDescription(description);
        stockController.removeButtonClicked();
        stockController.refreshButtonClicked();

        assertEquals(dao.findSoldItem(id).getQuantity(),quantity-new_quantity);


        //Item dose not excist
        id = 100L;
        stockController.setId(id);
        stockController.removeButtonClicked();
        stockController.refreshButtonClicked();
        assertEquals(dao.findSoldItem(id),null);

        //Quantity can not be negative
        Integer negative_quantity = -12;
        stockController.setQuantity(negative_quantity);
        stockController.removeButtonClicked();
        stockController.refreshButtonClicked();
        assertEquals(dao.findSoldItem(id).getQuantity(),quantity-new_quantity);

        //Price can not be nagative
        Double negative_price = -10.7;
        Integer new_new_quantity = 5;
        stockController.setPrice(negative_price);
        stockController.setQuantity(new_new_quantity);
        stockController.removeButtonClicked();
        stockController.refreshButtonClicked();
        assertEquals(dao.findSoldItem(id).getQuantity(),quantity-new_quantity);

        //Item can be removed without name and description
        id = 1L;
        quantity = dao.findStockItem(id).getQuantity();
        name = dao.findStockItem(id).getName();
        price = dao.findStockItem(id).getPrice();
        description = dao.findStockItem(id).getDescription();
        //New
        id = 1L;
        new_quantity = 12;
        price = 12.0;
        description = null;
        name = null;
        stockController.setId(id);
        stockController.setQuantity(new_quantity);
        stockController.setName(name);
        stockController.setPrice(price);
        stockController.setDescription(description);
        stockController.removeButtonClicked();
        stockController.refreshButtonClicked();

        assertEquals(dao.findSoldItem(id).getQuantity(),quantity-new_quantity);


    }
    @Test
    void addButtonClicked(){

        //Item Excist

        //Origninal
        Long id = 1L;
        Integer quantity = dao.findStockItem(id).getQuantity();
        String name = dao.findStockItem(id).getName();
        Double price = dao.findStockItem(id).getPrice();
        String description = dao.findStockItem(id).getDescription();
        //New
        id = 1L;
        Integer new_quantity = 12;
        price = 12.0;
        description = "tere1";
        name = "Tere1";
        stockController.setId(id);
        stockController.setQuantity(new_quantity);
        stockController.setName(name);
        stockController.setPrice(price);
        stockController.setDescription(description);
        stockController.addButtonClicked();
        stockController.refreshButtonClicked();


        assertEquals(dao.findSoldItem(id).getQuantity(),quantity+new_quantity);


        //Item dose not excist
        id = 100L;
        stockController.setId(id);
        stockController.addButtonClicked();
        stockController.refreshButtonClicked();
        assertEquals(dao.findSoldItem(id),null);

        //Quantity can not be negative
        Integer negative_quantity = -12;
        stockController.setQuantity(negative_quantity);
        stockController.addButtonClicked();
        stockController.refreshButtonClicked();
        assertEquals(dao.findSoldItem(id).getQuantity(),quantity+new_quantity);

        //Price can not be nagative
        Double negative_price = -10.7;
        Integer new_new_quantity = 5;
        stockController.setPrice(negative_price);
        stockController.setQuantity(new_new_quantity);
        stockController.addButtonClicked();
        stockController.refreshButtonClicked();
        assertEquals(dao.findSoldItem(id).getQuantity(),quantity+new_quantity);

        //Item can be added without name and description
        id = 1L;
        quantity = dao.findStockItem(id).getQuantity();
        name = dao.findStockItem(id).getName();
        price = dao.findStockItem(id).getPrice();
        description = dao.findStockItem(id).getDescription();
        //New
        id = 1L;
        new_quantity = 12;
        price = 12.0;
        description = null;
        name = null;
        stockController.setId(id);
        stockController.setQuantity(new_quantity);
        stockController.setName(name);
        stockController.setPrice(price);
        stockController.setDescription(description);
        stockController.addButtonClicked();
        stockController.refreshButtonClicked();

        assertEquals(dao.findSoldItem(id).getQuantity(),quantity+new_quantity);

    }

}