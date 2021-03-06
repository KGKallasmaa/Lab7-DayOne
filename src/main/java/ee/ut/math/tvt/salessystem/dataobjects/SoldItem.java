package ee.ut.math.tvt.salessystem.dataobjects;


import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;

import javax.persistence.*;
import java.util.Date;

/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
@Entity
@Table(name = "SoldItem")
public class SoldItem {

    @Id
    @Column(name="solditem_id")
  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  //  @OneToOne(targetEntity = StockItem.class)
  // @JoinColumn(name="stockitem_id")
    @Column(name="stockitem_id")
    private Long stockItem_id;
    @Column(name="quantity")
    private Integer quantity;
    @Column(name="time")
    private Long time;

    @Transient
    private Date date;
    @Transient
    private String name;
    @Transient
    private double price;
    @Transient
    private double sum;

    public SoldItem(){}
    public SoldItem(Long id, Long time, Long stockItem_id, int quantity, SalesSystemDAO dao) {
        StockItem stockItem = dao.findStockItem(stockItem_id);
        this.id = id;
        this.stockItem_id = stockItem_id;
        this.date = new Date(time);
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.sum = stockItem.getPrice() * this.quantity;
        this.time =time;
    }

    public SoldItem(StockItem stockItem, int quantity, Long time, Long id) {
        this.id = id;
        this.stockItem_id = stockItem.getId();
        this.date = new Date(time);
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.sum = stockItem.getPrice() * this.quantity;
        this.time = time;
    }

    public SoldItem(Date date,Long time, Double sum){
        this.date = date;
        this.time = time;
        this.sum = sum;
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

    public void setDate(Date date){
        this.date = date;
    }

    public double getSum() {
        return sum;
    }
    public void setSum(double sum){
        this.sum = sum;
    }

    public Long getStockItem_id() {
        return stockItem_id;
    }
    /*
    public StockItem getStockItem() {
        return dao.findStockItem(stockItem_id);
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }
    */

    @Override
    public String toString() {
        return "SoldItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", sum=" + sum +
                '}';
    }
}

