package ee.ut.math.tvt.salessystem.dataobjects;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Already bought StockItem. SoldItem duplicates name and price for preserving history.
 */
public class SoldItem {

    private Long id;
    private StockItem stockItem;
    private String name;
    private Integer quantity;
    private double price;
    private String date;
    private String time;
    private List<SoldItem> order_items;
    private double sum;

    public SoldItem() {
    }

    public SoldItem(StockItem stockItem, int quantity) {
        this.id = stockItem.getId();
        this.stockItem = stockItem;
        this.name = stockItem.getName();
        this.price = stockItem.getPrice();
        this.quantity = quantity;
    }
    public SoldItem(String date, String time, Double sum){
        this.date = date;
        this.time = time;
        this.sum = sum;
   //     System.out.println("date: " + this.date + " time: " + this.time + " sum: "+ this.sum);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDate (){ return date;}

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



    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getSum() {
        return price * ((double) quantity);
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
                ", order_items=" + order_items +
                ", sum=" + sum +
                '}';
    }
}

