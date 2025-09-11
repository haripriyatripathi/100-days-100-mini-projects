import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Expense {
    private String category;
    private double amount;
    private LocalDate date;
    private String note;

    public Expense(String category, double amount, String note) {
        this.category = category;
        this.amount = amount;
        this.date = LocalDate.now();
        this.note = note;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getNote() { return note; }

    @Override
    public String toString() {
        return date + " | " + category + " | ₹" + amount + " | " + note;
    }
}

class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();
    private static final String FILE_NAME = "expenses.csv";

    public void addExpense(Expense e) {
        expenses.add(e);
        saveToFile();
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }
        expenses.forEach(System.out::println);
    }

    public void generateReport() {
        double total = 0;
        Map<String, Double> categoryTotals = new HashMap<>();
        for (Expense e : expenses) {
            total += e.getAmount();
            categoryTotals.put(e.getCategory(),
                    categoryTotals.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }
        System.out.println("\n--- Expense Report ---");
        System.out.println("Total Spent: ₹" + total);
        categoryTotals.forEach((cat, amt) ->
                System.out.println(cat + ": ₹" + amt));
    }

    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.getDate() + "," + e.getCategory() + "," + e.getAmount() + "," + e.getNote());
            }
        } catch (IOException ex) {
            System.out.println("Error saving file.");
        }
    }

    public void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Expense e = new Expense(parts[1], Double.parseDouble(parts[2]), parts[3]);
                expenses.add(e);
            }
        } catch (IOException ex) {
            System.out.println("No previous data found.");
        }
    }
}

public class ExpenseTrackerApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();
        manager.loadFromFile();

        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Generate Report");
            System.out.println("4. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter category (Food/Travel/Shopping): ");
                    String cat = sc.nextLine();
                    System.out.print("Enter amount: ");
                    double amt = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter note: ");
                    String note = sc.nextLine();
                    manager.addExpense(new Expense(cat, amt, note));
                    System.out.println("Expense added successfully!");
                    break;
                case 2:
                    manager.viewExpenses();
                    break;
                case 3:
                    manager.generateReport();
                    break;
                case 4:
                    System.out.println("Exiting... Data saved.");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
