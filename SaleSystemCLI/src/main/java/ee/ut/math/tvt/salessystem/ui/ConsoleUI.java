package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
import javafx.scene.control.Tab;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;


/**
 * A simple CLI (limited functionality).
 */
public class ConsoleUI {
    private static final Logger log = LogManager.getLogger(ConsoleUI.class);

    private final SalesSystemDAO dao;
    private final ShoppingCart cart;

    public ConsoleUI(SalesSystemDAO dao) {
        this.dao = dao;
        cart = new ShoppingCart(dao);
    }

    public static void main(String[] args) throws Exception {
        SalesSystemDAO dao = new InMemorySalesSystemDAO();
        ConsoleUI console = new ConsoleUI(dao);
        console.run();
    }

    /**
     * Run the sales system CLI.
     */
    public void run() throws IOException {

        System.out.println("===========================");
        System.out.println("=       Sales System      =");
        System.out.println("===========================");
        printUsage();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.print("> ");
            processCommand(in.readLine().trim().toLowerCase());
            System.out.println("Done. ");
        }
    }

    private void showStock() {
        List<StockItem> stockItems = dao.findStockItems();
        System.out.println("-------------------------");
        for (StockItem si : stockItems) {
            System.out.println(si.getId() + " " + si.getName() + " " + si.getDescription() + " " + si.getPrice() + " Euro (" + si.getQuantity() + " items)");
        }
        if (stockItems.size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("-------------------------");
    }

    private void showCart() {
        System.out.println("-------------------------");
        for (SoldItem si : cart.getAll()) {
            System.out.println(si.getName() + " " + si.getPrice() + "Euro (" + si.getQuantity() + " items)");
        }
        if (cart.getAll().size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("-------------------------");
    }

    private void printUsage() {
        System.out.println("-------------------------");
        System.out.println("Usage:");
        System.out.println("h\t\tShow this help");
        System.out.println("w\t\tShow warehouse contents");
        System.out.println("atw\t\tAdd product to warehouse");
        System.out.println("rfw\t\tRemove product from warehouse");
        System.out.println("c\t\tShow cart contents");
        System.out.println("a IDX NR \tAdd NR of stock item with index IDX to the cart");
        System.out.println("p\t\tPurchase the shopping cart");
        System.out.println("r\t\tReset the shopping cart");
        System.out.println("ph\t\tShow purchase history");
        System.out.println("t\t\tShow team information");
        System.out.println("-------------------------");
    }

    private void processCommand(String command) {
        String[] c = command.split(" ");

        if (c[0].equals("h"))
            printUsage();
        else if (c[0].equals("q"))
            System.exit(0);
        else if (c[0].equals("w"))
            showStock();
        else if (c[0].equals("atw"))
            addProductToStock();
        else if (c[0].equals("rfw"))
            removeProductfromStock();
        else if (c[0].equals("c"))
            showCart();
        else if (c[0].equals("t"))
            showTeam();
        else if (c[0].equals("p"))
            cart.submitCurrentPurchase();
        else if (c[0].equals("r"))
            cart.cancelCurrentPurchase();
        else if (c[0].equals("hi"))
            showHistory();
        else if (c[0].equals("a") && c.length == 3) {
            try {
                long idx = Long.parseLong(c[1]);
                int amount = Integer.parseInt(c[2]);
                StockItem item = dao.findStockItem(idx);
                if (item != null) {
                    cart.addItem(new SoldItem(item, Math.min(amount, item.getQuantity())));
                } else {
                    System.out.println("no stock item with id " + idx);
                }
            } catch (SalesSystemException | NoSuchElementException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            System.out.println("unknown command");
        }
    }
        //It shows orders in 3 diffrent ways
    private void showHistory() {
        System.out.println("Please choose how you want to see it");
        System.out.println("1) between dates [1]");
        System.out.println("2) last 10 [2]");
        System.out.println("3) all [3]");
        Scanner sc = new Scanner(System.in);
        String history_type = sc.next();
        switch (history_type){
            case("1"):
                System.out.println("Please enter start date(LONG)");
                Long start_date = sc.nextLong();
                System.out.println("Please enter end date(LONG)");
                Long end_date = sc.nextLong();
                HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
                for(Long key : all_orders.keySet()){
                    List<SoldItem> order = all_orders.get(key);
                    if(key >= start_date && key <= end_date){
                        System.out.println(order);
                    }
                }
                break;
            case("2"):
                int i = 0;
                HashMap<Long,List<SoldItem>> last10_orders = dao.findAllOrders();
                Set<Long> keys = last10_orders.keySet();
                List<Long> keys_list = new ArrayList<>();
                keys_list.addAll(keys);
                Collections.sort(keys_list);
                Collections.reverse(keys_list);
                for(Long key : keys_list){
                    List<SoldItem> order = last10_orders.get(key);
                    if(i < 10){
                        System.out.println(order);
                        i++;
                    }
                }
                break;
            case("3"):
                for(List<SoldItem> el : dao.findAllOrders().values()){
                    System.out.println(el);
                }
                break;
        }
    }

    private void addProductToStock() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the id of the item");
        Long id = sc.nextLong();
        System.out.println("Please enter the name of the item");
        String name = sc.next();
        System.out.println("Please enter the description of the item");
        String desc = sc.next();
        System.out.println("Please enter the price of the item");
        Double price = sc.nextDouble();
        System.out.println("Please enter the quantity of the item");
        Integer quantity = sc.nextInt();
        StockItem item = new StockItem(id, name, desc, price, quantity);
        dao.saveStockItem(item);
        log.info("New item " + item.getName() + " saved.");
    }
    private void removeProductfromStock() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the id of the item");
        Long id = sc.nextLong();
        System.out.println("Please enter the quantity of the item");
        Integer quantity = sc.nextInt();
        StockItem item = dao.findStockItem(id);
        StockItem newCopy = new StockItem(item.getId(), item.getName(), item.getDescription(),item.getPrice(), quantity);
        dao.removeStockItem(newCopy);
        log.info(newCopy.getName() + " with quantity " + newCopy.getQuantity() + " was removed from warehouse");

    }

    private void showTeam() {

        Properties properties = null;

        try {
            properties = new Properties();
            properties.load(ConsoleUI.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            System.out.println("Properties file was not found");
        }
        System.out.println("-------------------------");
        System.out.println("Team information:");
        System.out.println("Team name:\t\t\t" + properties.getProperty("team_name"));       // team name
        System.out.println("Team leader:\t\t" + properties.getProperty("team_leader"));     // team leader
        System.out.println("Team leader email:\t" + properties.getProperty("team_leader_email"));// team leader email
        System.out.println("Team members:\t\t" + properties.getProperty("team_members"));   // team members
        System.out.println("-------------------------");


    }

    private void showOrderDetails() {
    }

}
