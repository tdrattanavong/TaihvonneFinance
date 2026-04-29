package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static double accountBalance;
    static boolean isRunning = true;

    static void main(String[] args) {

        //on app start we can update the balance
        accountBalance = loadTransactions().getLast().getBalance();

        System.out.println(accountBalance);
        homeScreen();
    }

    //MENUS

    //homeScreen
    static void homeScreen(){
        // we want the screen to continue showing until the application is ended

        while(isRunning) {

            System.out.println("""
                    Welcome!
                    D- Deposit
                    P- Payment
                    L- Ledger
                    X- Exit
                    """);
            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {

                case "D":
                    System.out.println("Adding deposit");
                    break;
                case "P":

                    break;

                case "L":
                ledgerScreen();
                    break;
                case "X":
                    //end of application
                    System.out.println("Adios! Bye");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Sorry invalid option! Please try again");

            } //end of switch statement
        } //end of the while loop

    }

    //ledger Screen
    static void ledgerScreen(){

        while(isRunning){
            System.out.println("""
                    Ledger Menu
                    A - View All
                    D = View All Deposits
                    P - View All Payments
                    R - Reports
                    B - Back Home
                    """);

            String choice = scanner.nextLine().trim().toUpperCase();
            switch (choice){
                case "A":
                    displayTransactions(loadTransactions());
                    break;
                case "D":

                    break;
                case "P":
                    System.out.println("Showing all Payments");
                    viewPayment();
                    break;
                case "R":
                    reportsScreen();
                    break;
                case "B":
                    System.out.println("Returning to Home Screen");
                    break;
                default:
            }

        }
    }

    static void reportsScreen(){
        while(isRunning) {
            System.out.println("""
                    
                    
                    
                    
                    B - Back
                    H - Return Home
                    """);

            String choice = scanner.nextLine().trim().toUpperCase();
            switch (choice) {

                case "B":
                    System.out.println("Back to Ledger Menu");
                    ledgerScreen();
                    break;
                case "H":
                    System.out.println("Returning to Home Menu");
                    homeScreen();
                    break;
            }
        }
    }

    // TODO: Make a new deposit transaction
    static void addDeposit(){

        //You should be generating the date and time
        //Hint:
        LocalDate todaysDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now(); // you will have to do logic to remove the nanoseconds at the end of the time

        System.out.println(todaysDate + " " + currentTime);


        System.out.println("Please add description");
        String description = scanner.nextLine();

        System.out.println("Vendor name");
        String vendor = scanner.nextLine();

        System.out.println("whats the deposit amount");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        //accountBalance = accountBalance + amount;
        accountBalance +=amount;

        Transaction transaction = new Transaction(todaysDate,currentTime,description,vendor,amount,accountBalance);

        //once you generate the transaction you want to save it to the file
        saveTransaction(transaction);

    }




    //TODO: Make a new payment transaction


    static void displayTransactions(ArrayList<Transaction> transactions){
        //display from most recent to least recent
        //use a traditional for loop to start at the end of the list
        //and work our way back to the beginning of it

        for(int i = transactions.size()-1; i >=0; i--){
            System.out.println(transactions.get(i));
        }
    }

    static void viewPayment(){
        //making a temporary array list to hold transactions that are  payments
        ArrayList<Transaction> tempPayArr = new ArrayList<>();

        for(Transaction item: loadTransactions()){
            if(item.getAmount() < 0){
                tempPayArr.add(item);
            }
        }

        //after we've added all the payment transactions to the tempPayArr
        //we can display those transactions
        displayTransactions(tempPayArr);
    }

    // TODO: view All deposits


    //loading transactions from the transactions.csv file
    static ArrayList<Transaction> loadTransactions(){
        ArrayList<Transaction> transactions = new ArrayList<>();
        String filename = "transactions.csv";
        //creating a try catch b/c an error can happen when trying to read the file
        //We are trying to READ form the file
        //bufferedReader/FileReader
        try(BufferedReader bR = new BufferedReader(new FileReader(filename))){

            //we want to create Transaction objects using the csv file
            //Each line in the csv file is a new Transaction

            String line = bR.readLine(); //read and discard the first line

            while((line=bR.readLine()) != null){

                String[] transactionArr = line.split("\\|");
                Transaction transactionObj = new Transaction(LocalDate.parse(transactionArr[0]),
                        LocalTime.parse(transactionArr[1]),transactionArr[2],transactionArr[3],
                        Double.parseDouble(transactionArr[4]),Double.parseDouble(transactionArr[5]));

                transactions.add(transactionObj);
            }

        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
        return transactions;
    }



    //TODO: saving transactions to the csv file
static void saveTransaction(Transaction transaction){

        //write(transaction.toString()) when ready to write
    //or just write(transaction)
}


}
