import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class MenuItem {
    private int id;
    private String name;
    private double price;

    public MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format(
                "%-5d %-20s ₹%.2f",
                id, name, price
        );
    }
}

public class RestaurantBillingSystem {

    static Scanner sc = new Scanner(System.in);

    // Stores menu items using item ID
    static HashMap<Integer, MenuItem> menu = new HashMap<>();

    // Stores selected items and their quantities
    static HashMap<Integer, Integer> order = new HashMap<>();

    // GST rate
    static final double GST_RATE = 0.05;

    // Add default menu items
    static void initializeMenu() {
        menu.put(1, new MenuItem(1, "Veg Biryani", 180));
        menu.put(2, new MenuItem(2, "Chicken Biryani", 250));
        menu.put(3, new MenuItem(3, "Paneer Butter Masala", 200));
        menu.put(4, new MenuItem(4, "Fried Rice", 150));
        menu.put(5, new MenuItem(5, "Masala Dosa", 100));
        menu.put(6, new MenuItem(6, "Fresh Lime Soda", 60));
    }

    // Display menu
    static void displayMenu() {

        System.out.println("\n============== MENU ==============");

        System.out.printf(
                "%-5s %-20s %-10s%n",
                "ID", "Item", "Price"
        );

        System.out.println("-----------------------------------");

        for (MenuItem item : menu.values()) {
            System.out.println(item);
        }

        System.out.println("===================================");
    }

    // Add new menu item dynamically
    static void addMenuItem() {

        System.out.println("\n===== Add Menu Item =====");

        System.out.print("Enter item ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (menu.containsKey(id)) {
            System.out.println("Item ID already exists.");
            return;
        }

        System.out.print("Enter item name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }

        System.out.print("Enter item price: ₹");
        double price = sc.nextDouble();

        if (price <= 0) {
            System.out.println("Price must be greater than zero.");
            return;
        }

        menu.put(id, new MenuItem(id, name, price));

        System.out.println("Menu item added successfully.");
    }

    // Remove menu item dynamically
    static void removeMenuItem() {

        displayMenu();

        System.out.print("\nEnter item ID to remove: ");
        int id = sc.nextInt();

        if (!menu.containsKey(id)) {
            System.out.println("Item not found.");
            return;
        }

        menu.remove(id);

        // Remove from current order if present
        order.remove(id);

        System.out.println("Menu item removed successfully.");
    }

    // Add item to order
    static void addToOrder() {

        displayMenu();

        System.out.print("\nEnter item ID: ");
        int id = sc.nextInt();

        if (!menu.containsKey(id)) {
            System.out.println("Invalid item ID.");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        if (quantity <= 0) {
            System.out.println(
                    "Quantity must be greater than zero."
            );
            return;
        }

        order.put(
                id,
                order.getOrDefault(id, 0) + quantity
        );

        System.out.println(
                quantity + " x " +
                menu.get(id).getName() +
                " added to order."
        );
    }

    // Remove item from order
    static void removeFromOrder() {

        if (order.isEmpty()) {
            System.out.println("Your order is empty.");
            return;
        }

        displayOrder();

        System.out.print("\nEnter item ID to remove: ");
        int id = sc.nextInt();

        if (!order.containsKey(id)) {
            System.out.println("Item is not in the order.");
            return;
        }

        order.remove(id);

        System.out.println("Item removed from order.");
    }

    // Display current order
    static void displayOrder() {

        if (order.isEmpty()) {
            System.out.println("\nYour order is empty.");
            return;
        }

        System.out.println("\n============= CURRENT ORDER =============");

        System.out.printf(
                "%-5s %-20s %-8s %-12s%n",
                "ID", "Item", "Qty", "Amount"
        );

        System.out.println("------------------------------------------");

        double subtotal = 0;

        for (Map.Entry<Integer, Integer> entry : order.entrySet()) {

            int id = entry.getKey();
            int quantity = entry.getValue();

            MenuItem item = menu.get(id);

            if (item == null) {
                continue;
            }

            double amount = item.getPrice() * quantity;
            subtotal += amount;

            System.out.printf(
                    "%-5d %-20s %-8d ₹%-11.2f%n",
                    id,
                    item.getName(),
                    quantity,
                    amount
            );
        }

        System.out.println("------------------------------------------");
        System.out.printf(
                "Subtotal: ₹%.2f%n",
                subtotal
        );

        System.out.println("==========================================");
    }

    // Generate final bill
    static void generateBill() {

        if (order.isEmpty()) {
            System.out.println(
                    "\nCannot generate bill. Order is empty."
            );
            return;
        }

        double subtotal = 0;

        System.out.println("\n==========================================");
        System.out.println("             RESTAURANT BILL");
        System.out.println("==========================================");

        System.out.printf(
                "%-20s %-8s %-12s%n",
                "Item", "Qty", "Amount"
        );

        System.out.println("------------------------------------------");

        for (Map.Entry<Integer, Integer> entry : order.entrySet()) {

            int id = entry.getKey();
            int quantity = entry.getValue();

            MenuItem item = menu.get(id);

            if (item == null) {
                continue;
            }

            double amount = item.getPrice() * quantity;
            subtotal += amount;

            System.out.printf(
                    "%-20s %-8d ₹%-11.2f%n",
                    item.getName(),
                    quantity,
                    amount
            );
        }

        double gst = subtotal * GST_RATE;
        double total = subtotal + gst;

        System.out.println("------------------------------------------");

        System.out.printf(
                "Subtotal:       ₹%.2f%n",
                subtotal
        );

        System.out.printf(
                "GST (5%%):       ₹%.2f%n",
                gst
        );

        System.out.printf(
                "Grand Total:    ₹%.2f%n",
                total
        );

        System.out.println("==========================================");
        System.out.println("       Thank you! Visit Again!");
        System.out.println("==========================================");

        // Clear order after bill generation
        order.clear();
    }

    // Main program
    public static void main(String[] args) {

        initializeMenu();

        while (true) {

            System.out.println("\n================================");
            System.out.println("    RESTAURANT BILLING SYSTEM");
            System.out.println("================================");
            System.out.println("1. View Menu");
            System.out.println("2. Add Menu Item");
            System.out.println("3. Remove Menu Item");
            System.out.println("4. Add Item to Order");
            System.out.println("5. Remove Item from Order");
            System.out.println("6. View Current Order");
            System.out.println("7. Generate Bill");
            System.out.println("8. Exit");
            System.out.println("================================");

            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println(
                        "Invalid input. Enter a number."
                );
                sc.next();
                continue;
            }

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    displayMenu();
                    break;

                case 2:
                    addMenuItem();
                    break;

                case 3:
                    removeMenuItem();
                    break;

                case 4:
                    addToOrder();
                    break;

                case 5:
                    removeFromOrder();
                    break;

                case 6:
                    displayOrder();
                    break;

                case 7:
                    generateBill();
                    break;

                case 8:
                    System.out.println(
                            "Thank you for using the system!"
                    );
                    sc.close();
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please select 1-8."
                    );
            }
        }
    }
}