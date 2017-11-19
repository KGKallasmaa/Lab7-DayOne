package ee.ut.math.tvt.salessystem.dataobjects;


import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;

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

    @Transient
    private Date date;

    @OneToOne(targetEntity = StockItem.class)
    @JoinColumn(name="stockitem_id")
    private Long stockItem_id;

    @Column(name="quantity")
    private Integer quantity;

    @Transient
    private String name;
    @Transient
    private double price;
    @Column(name="time")
    private Long time;
    @Transient
    private double sum;

    public SoldItem(){}
    public SoldItem(Long id, Long time,Long stockItem_id, int quantity) {
        SalesSystemDAO dao = new HibernateSalesSystemDAO();
        StockItem stockItem = dao.findStockItem(stockItem_id);
        this.id = id;
        this.stockItem_id = stockItem_id;
        this.date = new Date(time);
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
        this.sum = stockItem.getPrice() * quantity;
        this.time =time;
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

