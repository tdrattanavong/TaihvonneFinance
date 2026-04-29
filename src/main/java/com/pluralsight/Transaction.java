package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
//date|time|description|vendor|amount|balance
private LocalDate date;
private LocalTime time;
private String description;
private String vendor;
private double amount;
private double balance;


    public Transaction() {
    }

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
        //no need to add balance to this constructor because the balance should
        //be determined by the deposit or payment being made
        //this constructor can be used when creating a transaction
    }

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount, double balance) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
        this.balance = balance;
        //this constructor will be used when reading from or saving to the csv file
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //toString menthod could come in handy when it time to save a transaction
    //aka 'write to the transaction.csv file'

    @Override
    public String toString() {
//        return "Transaction{" +
//                "date=" + date +
//                ", time=" + time +
//                ", description='" + description + '\'' +
//                ", vendor='" + vendor + '\'' +
//                ", amount=" + amount +
//                ", balance=" + balance +
//                '}';

        return date+"|"+time+"|"+description+"|"+vendor+"|"+amount+"|"+balance;


    }
}
