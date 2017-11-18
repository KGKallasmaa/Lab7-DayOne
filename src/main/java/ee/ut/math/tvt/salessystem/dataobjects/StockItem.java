package ee.ut.math.tvt.salessystem.dataobjects;


import javax.persistence.*;
import java.util.Date;

/**
 * Stock item.
 */

@Entity
@Table(name = "Stockitem")
public class StockItem {

    @Id
    @Column(name="stockitem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name",nullable = false)
   // @Column(nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    public StockItem(Long id, String name, String description,Double price, int quantity) {
        this.name = name;
        this.id = id;
        this.price =price;
        this.description = description;
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("StockItem{id=%d, name='%s'}", id, name);
    }
}
