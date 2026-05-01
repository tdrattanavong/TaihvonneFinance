package com.pluralsight;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static double accountBalance = 0.0;
    static final String FILENAME = "transactions.csv";
    static final String CSV_HEADER = "date|time|description|vendor|amount|balance";

    public static void main(String[] args) {
        // Load balance from last transaction in file (if any)
        ArrayList<Transaction> existing = loadTransactions();
        if (!existing.isEmpty()) {
            accountBalance = existing.getLast().getBalance();
        }

        System.out.printf("Ledger started. Current balance: $%.2f%n", accountBalance);
        homeScreen();
    }

    //  SCREENS

    static void homeScreen() {
        while (true) {
            System.out.println("""
                    
                    
                         Menu
                    
                      D - Deposit
                      P - Payment
                      L - Ledger
                      X - Exit
                    
                    """);

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D" -> addDeposit();
                case "P" -> addPayment();
                case "L" -> ledgerScreen();
                case "X" -> {
                    System.out.println("Take Care, Bye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    static void ledgerScreen() {
        while (true) {
            System.out.println("""
                    
                    ── Ledger Menu ──────────────────
                      A - View All Transactions
                      D - View Deposits Only
                      P - View Payments Only
                      R - Reports
                      B - Back to Home
                   
                    """);

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A" -> displayTransactions(loadTransactionsReversed());
                case "D" -> viewDeposits();
                case "P" -> viewPayments();
                case "R" -> reportsScreen();
                case "B" -> { return; }   // ← actually exits the loop
                default  -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    static void reportsScreen() {
        while (true) {
            System.out.println("""
                    
                    ── Reports ──────────────────────
                      1 - Month to Date
                      2 - Previous Month
                      3 - Year to Date
                      4 - Previous Year
                      5 - Search by Vendor
                      0 - Back to Ledger
                   
                    """);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> displayTransactions(monthToDate());
                case "2" -> displayTransactions(previousMonth());
                case "3" -> displayTransactions(yearToDate());
                case "4" -> displayTransactions(previousYear());
                case "5" -> searchByVendor();
                case "0" -> { return; }   // ← actually exits the loop
                default  -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //  ADD TRANSACTIONS

    static void addDeposit() {
        System.out.println("\n── New Deposit ──────────────────");
        try {
            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Vendor/Source: ");
            String vendor = scanner.nextLine().trim();

            System.out.print("Amount: $");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }

            accountBalance += amount;

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now().withNano(0); // strip nanoseconds

            Transaction t = new Transaction(date, time, description, vendor, amount, accountBalance);
            saveTransaction(t);
            System.out.printf("Deposit saved! New balance: $%.2f%n", accountBalance);

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }

    static void addPayment() {
        System.out.println("\n── New Payment ──────────────────");
        try {
            System.out.print("Description: ");
            String description = scanner.nextLine().trim();

            System.out.print("Vendor: ");
            String vendor = scanner.nextLine().trim();

            System.out.print("Amount: $");
            double amount = Double.parseDouble(scanner.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Payment amount must be positive.");
                return;
            }

            accountBalance -= amount;  // payments are stored as negative

            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now().withNano(0);

            Transaction t = new Transaction(date, time, description, vendor, -amount, accountBalance);
            saveTransaction(t);
            System.out.printf("Payment saved! New balance: $%.2f%n", accountBalance);

        } catch (NumberFormatException e) {
            System.out.println("Invalid amount. Please enter a number.");
        }
    }

    //  Display

    static void displayTransactions(ArrayList<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        System.out.println();
        System.out.printf("%-12s %-10s %-25s %-20s %10s %12s%n",
                "Date", "Time", "Description", "Vendor", "Amount", "Balance");
        System.out.println("─".repeat(95));
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println("─".repeat(95));
        System.out.printf("Total transactions: %d%n", transactions.size());
    }

    static void viewDeposits() {
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction t : loadTransactionsReversed()) {
            if (t.getAmount() > 0) deposits.add(t);
        }
        displayTransactions(deposits);
    }

    static void viewPayments() {
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction t : loadTransactionsReversed()) {
            if (t.getAmount() < 0) payments.add(t);
        }
        displayTransactions(payments);
    }

    //  REPORTS

    static ArrayList<Transaction> monthToDate() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end   = LocalDate.now();
        return filterByDateRange(start, end);
    }

    static ArrayList<Transaction> previousMonth() {
        LocalDate firstOfThisMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate start = firstOfThisMonth.minusMonths(1);
        LocalDate end   = firstOfThisMonth.minusDays(1);
        return filterByDateRange(start, end);
    }

    static ArrayList<Transaction> yearToDate() {
        LocalDate start = LocalDate.now().withDayOfYear(1);
        LocalDate end   = LocalDate.now();
        return filterByDateRange(start, end);
    }

    static ArrayList<Transaction> previousYear() {
        int lastYear = LocalDate.now().getYear() - 1;
        LocalDate start = LocalDate.of(lastYear, 1, 1);
        LocalDate end   = LocalDate.of(lastYear, 12, 31);
        return filterByDateRange(start, end);
    }

    static void searchByVendor() {
        System.out.print("Enter vendor name to search: ");
        String query = scanner.nextLine().trim().toLowerCase();

        ArrayList<Transaction> results = new ArrayList<>();
        for (Transaction t : loadTransactionsReversed()) {
            if (t.getVendor().toLowerCase().contains(query)) {
                results.add(t);
            }
        }
        displayTransactions(results);
    }

    static ArrayList<Transaction> filterByDateRange(LocalDate start, LocalDate end) {
        ArrayList<Transaction> result = new ArrayList<>();
        for (Transaction t : loadTransactionsReversed()) {
            LocalDate d = t.getDate();
            if (!d.isBefore(start) && !d.isAfter(end)) {
                result.add(t);
            }
        }
        return result;
    }

    // ─────────────────────────────────────────────
    //  FILE I/O
    // ─────────────────────────────────────────────

    static ArrayList<Transaction> loadTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Path path = Path.of(FILENAME);

        if (!Files.exists(path)) return transactions;  // no file yet — that's fine

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            br.readLine(); // skip header

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length < 6) continue; // skip malformed rows

                Transaction t = new Transaction(
                        LocalDate.parse(parts[0]),
                        LocalTime.parse(parts[1]),
                        parts[2],
                        parts[3],
                        Double.parseDouble(parts[4]),
                        Double.parseDouble(parts[5])
                );
                transactions.add(t);
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }

    static ArrayList<Transaction> loadTransactionsReversed() {
        ArrayList<Transaction> list = loadTransactions();
        Collections.reverse(list);
        return list;
    }

    static void saveTransaction(Transaction transaction) {
        Path path = Path.of(FILENAME);
        boolean fileExists = Files.exists(path);

        // true = append mode — never overwrites existing data
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME, true))) {
            if (!fileExists) {
                bw.write(CSV_HEADER);
                bw.newLine();
            }
            bw.write(transaction.toString());
            bw.newLine();
        } catch (Exception e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }
}