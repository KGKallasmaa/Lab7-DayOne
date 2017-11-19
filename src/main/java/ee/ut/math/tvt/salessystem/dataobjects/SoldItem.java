package ee.ut.math.tvt.salessystem.dataobjects;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
@Entity
@Table(name = "SoldItem")
public class SoldItem {

    @Id
    @Column(name="solditem_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="date")
    private Date date;

    @OneToOne(targetEntity = StockItem.class)
    @JoinColumn(name="stockitem_id")
    private StockItem stockItem;

    @Column(name="quantity",nullable = false)
    private Integer quantity;

    @Transient
    private String name;
    @Transient
    private double price;
    @Transient
    private Long time;
    @Transient
    private double sum;

    public SoldItem(){}
    public SoldItem(Date date,StockItem stockItem, int quantity) {
        this.id = stockItem.getId();
        this.stockItem = stockItem;
        this.date = date;
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.sum = stockItem.getPrice() * quantity;
        this.time =date.getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Date getDate (){ return date;}

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

    public Long getTime() {
        return time;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return sum;
    }
    public void setSum(double sum){
        this.sum = sum;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    @Override
    public String toString() {
        return "SoldItem{" +
                "id=" + id +
                ", stockItem=" + stockItem +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", sum=" + sum +
                '}';
    }
}

