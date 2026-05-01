package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;
    private double balance;

    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount, double balance) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
        this.balance = balance;
    }

    public LocalDate getDate()        { return date; }
    public LocalTime getTime()        { return time; }
    public String getDescription()    { return description; }
    public String getVendor()         { return vendor; }
    public double getAmount()         { return amount; }
    public double getBalance()        { return balance; }

    @Override
    public String toString() {
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("%s|%s|%s|%s|%.2f|%.2f",
                date,
                time.format(timeFmt),
                description,
                vendor,
                amount,
                balance);
    }

}
