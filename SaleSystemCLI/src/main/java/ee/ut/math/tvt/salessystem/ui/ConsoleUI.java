package ee.ut.math.tvt.salessystem.ui;

import ee.ut.math.tvt.salessystem.SalesSystemException;
import ee.ut.math.tvt.salessystem.dao.HibernateSalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.InMemorySalesSystemDAO;
import ee.ut.math.tvt.salessystem.dao.SalesSystemDAO;
import ee.ut.math.tvt.salessystem.dataobjects.SoldItem;
import ee.ut.math.tvt.salessystem.dataobjects.StockItem;
import ee.ut.math.tvt.salessystem.logic.ShoppingCart;
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
        //SalesSystemDAO dao = new HibernateSalesSystemDAO();
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
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println();
        int accountType = accountSelect();
        //System.out.println();
        while (true){
            System.out.print("> ");
            processCommand(in.readLine().trim().toLowerCase(), accountType);
            System.out.println("Done. ");
        }
    }

    private int accountSelect() throws IOException{
        int accountType;
        System.out.println("-------------------------");
        System.out.println("Please select an account type:");
        System.out.println("\t1 - Cashier");
        System.out.println("\t2 - Warehouse");
        System.out.println("\t3 - Admin");
        System.out.println("-------------------------");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            System.out.print(">");
            String input = in.readLine().trim();
            if (input.equals("1") | input.equals("2") | input.equals("3")){
                accountType = Integer.parseInt(input);
                break;
            }
            System.out.println("Invalid input, try again.");
        }
        System.out.println(String.format("Selected account type: %s", (
                (accountType == 1) ? "Cashier" :
                (accountType == 2) ? "Warehouse" :
                (accountType == 3) ? "Admin" :
                "Error, should not be reached")));
        //System.out.println();
        printUsage(accountType);
        return accountType;
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
        for (StockItem si : cart.getAll()) {
            System.out.println(si.getName() + " " + si.getPrice() + "Euro (" + si.getQuantity() + " items)");
        }
        if (cart.getAll().size() == 0) {
            System.out.println("\tNothing");
        }
        System.out.println("-------------------------");
    }
    private void printUsage(int accountType) {
        System.out.println("-------------------------");
        System.out.println("Usage:");
        System.out.println("h\t\tShow this help");
        System.out.println("q\t\tExit sales system");
        //POS
        if (accountType == 1 | accountType == 3) {
            System.out.println("c\t\tShow cart contents");
            System.out.println("a IDX NR \tAdd NR of stock item with index IDX to the cart");
            System.out.println("p\t\tPurchase the shopping cart");
            System.out.println("r\t\tReset the shopping cart");
        }
        //Warehouse
        if (accountType == 2 | accountType == 3) {
            System.out.println("w\t\tShow warehouse contents");
            System.out.println("atw\t\tAdd product to warehouse");
            System.out.println("rfw\t\tRemove product from warehouse");
        }
        //History
        if (accountType == 3) {
            System.out.println("ph\t\tShow purchase history");
        }
        //Team
        System.out.println("t\t\tShow team information");
        System.out.println("ca\t\tChange account type");
        System.out.println("q\t\tExit sales system");
        System.out.println("-------------------------");
    }
    private void processCommand(String command, int accountType) throws IOException {
        String[] c = command.split(" ");

        if (c[0].equals("h"))
            printUsage(accountType);
        else if (c[0].equals("q"))
            System.exit(0);
        else if (c[0].equals("w") && (accountType == 2 | accountType == 3))// warehouse and admin access
            showStock();
        else if (c[0].equals("atw") && (accountType == 2 | accountType == 3))// warehouse and admin access
            addProductToStock();
        else if (c[0].equals("rfw") && (accountType == 2 | accountType == 3))// warehouse and admin access
            removeProductfromStock();
        else if (c[0].equals("c") && (accountType == 1 | accountType == 3))// cashier and admin access
            showCart();
        else if (c[0].equals("t") && (accountType == 1 | accountType == 3))// cashier and admin access
            showTeam();
        else if (c[0].equals("p") && (accountType == 1 | accountType == 3))// cashier and admin access
            cart.submitCurrentPurchase();
        else if (c[0].equals("r") && (accountType == 1 | accountType == 3))// cashier and admin access
            cart.cancelCurrentPurchase();
        else if (c[0].equals("ph") && accountType == 3)// admin access
            showHistory();
        else if (c[0].equals("ca"))
            accountSelect();
        else if (c[0].equals("a") && c.length == 3  && (accountType == 1 | accountType == 3)) {// cashier and admin access
            try {
                long idx = Long.parseLong(c[1]);
                int amount = Integer.parseInt(c[2]);
                StockItem item = dao.findStockItem(idx);
                if (item != null) {
                    cart.addItem(item, amount);
                } else {
                    System.out.println("no stock item with id " + idx);
                    System.out.print(">");
                }
            } catch (SalesSystemException | NoSuchElementException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            System.out.println("unknown command");
        }
    }
    //It shows order history in 3 diffrent ways
    private void showHistory() {
        System.out.println("Please choose how you want to see it");
        System.out.println("1) between dates [1]");
        System.out.println("2) last 10 [2]");
        System.out.println("3) all [3]");
        System.out.print(">");
        Scanner sc = new Scanner(System.in);
        String history_type = sc.next();
        switch (history_type){
            case("1"):
                System.out.println("Please enter start date(LONG)");
                System.out.print(">");
                Long start_date = sc.nextLong();
                System.out.println("Please enter end date(LONG)");
                System.out.print(">");
                Long end_date = sc.nextLong();
                HashMap<Long,List<SoldItem>> all_orders1 = dao.findAllOrders();
                for(Long key : all_orders1.keySet()){
                    List<SoldItem> order = all_orders1.get(key);
                    if(key >= start_date && key <= end_date){
                        System.out.println(new Date(order.get(0).getTime()));
                        showOrderDetails(order);
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
                        System.out.println(new Date(order.get(0).getTime()));
                        showOrderDetails(order);
                        i++;
                    }
                }
                break;
            case("3"):
                HashMap<Long,List<SoldItem>> all_orders = dao.findAllOrders();
                Set<Long> keys1 = all_orders.keySet();
                List<Long> keys_list1 = new ArrayList<>();
                keys_list1.addAll(keys1);
                Collections.sort(keys_list1);
                Collections.reverse(keys_list1);
                for(Long key : keys_list1){
                    List<SoldItem> order = all_orders.get(key);
                    System.out.println(new Date(order.get(0).getTime()));
                    showOrderDetails(order);
                }
                break;
        }
    }
    private void addProductToStock() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the id of the item");
        System.out.print(">");
        Long id = sc.nextLong();
        List<StockItem> allItems = dao.findStockItems();
        for (StockItem item : allItems) {
            if (Objects.equals(item.getId(), id)) {// if exists then ask only for additional quantity
                System.out.println("Found " + item.getName() + " with id " + item.getId() + " and existing quantity " + item.getQuantity() + ". Please enter additional quantity.");
                System.out.print(">");
                Integer quantity = sc.nextInt();
                while (quantity <= 0) { // can be equal with 0 because then allows accidental wrong id inputs without changing quantity
                    System.out.println("Please enter a positive quantity, if you don't want to change the quantity enter 0");
                    System.out.print(">");
                    quantity = sc.nextInt();
                }
                System.out.println("Please enter price of added items.");
                System.out.print(">");
                Integer newItemPrice = sc.nextInt();
                while(newItemPrice <= 0){
                    System.out.println("Please enter a price that is larger than 0");
                    System.out.print(">");
                    quantity = sc.nextInt();
                }
                StockItem item_tobe_added = new StockItem(item);
                item_tobe_added.setQuantity(quantity);
                item_tobe_added.setPrice(newItemPrice);
                dao.saveStockItem(item_tobe_added);
                return;
            }
        }
        System.out.println("Please enter the name of the item");
        System.out.print(">");
        String name = sc.next();
        System.out.println("Please enter the description of the item");
        System.out.print(">");
        String desc = sc.next();
        System.out.println("Please enter the price of the item");
        System.out.print(">");
        Double price = sc.nextDouble();
        while(price<0){
            System.out.println("Price can not be negative, please enter a new price");
            System.out.print(">");
            price = sc.nextDouble();
        }
        System.out.println("Please enter the quantity of the item");
        System.out.print(">");
        Integer quantity = sc.nextInt();
        while(quantity<0){
            System.out.println("Quantity has to be larger than 0");
            System.out.print(">");
            quantity = sc.nextInt();
        }
        StockItem item = new StockItem(id, name, desc, price, quantity);
        dao.saveStockItem(item);
        log.info("New item " + item.getName() + " saved.");
    }
    private void removeProductfromStock() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the id of the item");// catch error if index not found
        System.out.print(">");
        Long id = sc.nextLong();
        System.out.println("Please enter the quantity of the item you wish to remove");
        System.out.print(">");
        Integer quantity = sc.nextInt();
        StockItem item = dao.findStockItem(id);
        if(quantity > item.getQuantity()){
            quantity = item.getQuantity();
        }
        StockItem newCopy = new StockItem(item.getId(), item.getName(), item.getDescription(),item.getPrice(), quantity);
        dao.removeStockItem(newCopy, false);
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
    private void showOrderDetails(List<SoldItem> order) {

        for (SoldItem item : order){
            String name = dao.findStockItem(item.getStockItem_id()).getName();
            Long id = dao.findStockItem(item.getStockItem_id()).getId();
            System.out.println("ID: " + id + " Name: " + name + " Quantity: " + item.getQuantity());
        }
    }

}
