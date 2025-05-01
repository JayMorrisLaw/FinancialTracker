package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class FinancialTracker {
//using an array list so it can grow or be changed as needed, final keeps the constants from being edited accidentally
    // also using dateTime formatter to correctly format and parse string dates to LocalDate/Time
    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // displaying a menu and prompting/waiting for user input
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            // depending on the input from the user itll call a specific method (addDeposit or paayment etc)
            String input = scanner.nextLine().trim();
            // using switch case to check for different options
            // the toUppercase makes it so that D or d will work regardless of its case
            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
                    // break allows you to exit the while loop early
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String filename) {
        String line;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            // using a buffered reader to read from the file line by line, filereader opens the file
            while ((line = bufferedReader.readLine()) != null) {
                // this while loop uses buffered reader to read lines until there are no more (null)
                String[] parts = line.split("\\|");
                // splits the lines into parts where the | is
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                // parsing LocalDate time and double makes sure each vaule is the right data type
                transactions.add(new Transaction(date, time, description, vendor, amount));
                // adds a new transaction object with the parts we just got
            }
            bufferedReader.close();
            // closes after were done reading
        } catch (Exception e) {
            // catches errors
            System.out.println("Error loading file transactions: ");
            // this method opens reads and splits the file into different lines and turns the text into an object

        }

    }

    private static void addDeposit(Scanner scanner) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
           // using buffered writer to write to file
            System.out.println("Enter the date (YYYY-MM-DD) : ");
            String date = scanner.nextLine();
            // prompts the user to enter the date in the correct format
            LocalDate dateInput = LocalDate.parse(date, DATE_FORMATTER);
            // parse converts the string into a localdate object
            System.out.println("Enter the time (HH:MM:SS) : ");
            String time = scanner.nextLine();
            LocalTime timeInput = LocalTime.parse(time, TIME_FORMATTER);
            System.out.println("Enter the description: ");
            String description = scanner.nextLine();
            System.out.println("Enter the vendor: ");
            String vendor = scanner.nextLine();
            System.out.println("Enter the amount: ");
            Double amount = scanner.nextDouble();
            scanner.nextLine();
            Transaction transaction = new Transaction(dateInput, timeInput, description, vendor, amount);
            // creates a transaction based off user entry
            System.out.println("Successfully added deposit! " + transaction);
            transactions.add(transaction);
            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();
            // closes file

        } catch (Exception e) {
            System.out.println("error has occured");
        }

    }

    private static void addPayment(Scanner scanner) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true));
            // opens the file and appends to it
            System.out.println("Enter the date (YYYY-MM-DD) : ");
            String date = scanner.nextLine();
            // prmpting user to enter what theyre asked in the correct format using the scanner
            LocalDate dateInput = LocalDate.parse(date, DATE_FORMATTER);
            // date formatter makes sure the date is formatted correctly
            System.out.println("Enter the time (HH:MM:SS) : ");
            String time = scanner.nextLine();
            LocalTime timeInput = LocalTime.parse(time, TIME_FORMATTER);
            System.out.println("Enter the description: ");
            String description = scanner.nextLine();
            System.out.println("Enter the vendor: ");
            String vendor = scanner.nextLine();
            System.out.println("Enter the amount: ");
            Double amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount > 0) {
                amount = -amount;
                // using an if statement to filp the entered amount into a negative number because we are making payments
            }
            Transaction transaction = new Transaction(dateInput, timeInput, description, vendor, amount);
            // makes new transaction based off user entry
            System.out.println("Successful payment! " + transaction);
            transactions.add(transaction);
            bufferedWriter.write(transaction.toString());
            bufferedWriter.newLine();
            bufferedWriter.close();

        } catch (Exception e) {
            System.out.println("error has occured");
        }
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            // creates a menu and runs through the while loop
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        System.out.printf("%-12s| %-8s |%-30s |%-30s | %-9s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        // printing a table header , using %- to define the spacing for each column(left align)
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            // for loop to go through each transaction
            System.out.printf("%-12s| %-8s |%-30s |%-22s | %9.2f\n", // %9.2f prints a float using 9 total spaces
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getDescription(),
                    transaction.getVendor(),
                    transaction.getAmount());
           // grabs the transaction attributes
        }
    }

    private static void displayDeposits() {
        System.out.printf("%-12s| %-8s |%-30s |%-20s | %-9s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                // if statement is used to display a positive amount for deposits / filters out payments
                System.out.printf("%-12s| %-8s |%-30s |%-12s | %9.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }
            // This method should display a table of all deposits in the `transactions` ArrayList.
            // The table should have columns for date, time, description, vendor, and amount.
        }
    }

    private static void displayPayments() {
        System.out.printf("%-12s| %-8s |%-30s |%-30s | %-9s\n",
                "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-------------------------------------------------------------------------------------------------");
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                // if statement is used to filter out deposits and show payments , shows negative amounts
                System.out.printf("%-12s| %-8s |%-30s |%-20s | %9.2f\n",
                        transaction.getDate(),
                        transaction.getTime(),
                        transaction.getDescription(),
                        transaction.getVendor(),
                        transaction.getAmount());
            }

        }


    }
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            // using a while loop to display a reports menu
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();// trims extra input

            switch (input) {
                case "1":
                    LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
                    LocalDate today = LocalDate.now();
                    filterTransactionsByDate(monthStart, today);
                    break;
                   /* typing 1 gets todays date and withdayofmonth sets the date to the 1st of this month
                   creating 2 dates the first of the month and today. filtertransaction method filters
                   between the 2
                    */
                case "2":
                    LocalDate lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate lastMonthEnd = LocalDate.now().withDayOfMonth(1).minusDays(1);
                    filterTransactionsByDate(lastMonthStart, lastMonthEnd);
                    break;
                   /* minus 1 months subtracts a month from today, withdayofmonth sets the day to the 1st of the month
                  second line takes the 1st day of this month and subs 1 day to make it the last day of last month
                  the method filters between dates
                    */
                case "3":
                    LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
                    LocalDate todaysDate = LocalDate.now();
                    filterTransactionsByDate(startOfYear, todaysDate);
                    break;
                    /* gets first of the year , gets todays date, method filters between the 2 dates

                     */
                case "4":
                    LocalDate firstDayOfCurrentYear = LocalDate.now().withDayOfYear(1);
                    LocalDate lastDayOfLastYear = firstDayOfCurrentYear.minusDays(1);
                    LocalDate firstOfLastYear = lastDayOfLastYear.withDayOfYear(1);
                    filterTransactionsByDate(firstOfLastYear, lastDayOfLastYear);
                    break;
                    /*
                   4 gets first of this year, subtracts 1 day from the first of this year to get the last day of
                   last year, takes the last day of last year and sets it to the 1st of the previous year
                     */
                case "5":
                    System.out.println("Enter vendor name: ");
                    String name = scanner.nextLine();
                    filterTransactionsByVendor(name);
                    break;
                    /*
                    grabs the vendor name
                     */
                case "0":
                    running = false;
                    // exit loop
                default:
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {

         boolean found = false;

        for (Transaction transaction : transactions) {
            /*
            loop to go through each trans 1by1
             */
            LocalDate transactionDate = transaction.getDate();// gets date of trans and saves it
            if ((transactionDate.equals(startDate) || transactionDate.isAfter(startDate)) &&
            (transactionDate.equals(endDate) || transactionDate.isBefore(endDate))){
                System.out.println(transaction);
                found = true;
                   /*
                 checks if the trans date is on or after the start date / checks if trans date is on or before
                 end date
                     */
            }
        }
        if (!found){
            System.out.println("No transactions found ");
        }

    }
    private static void filterTransactionsByVendor(String vendor) {
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)){
                /*
                if the vendor name from the trans matches the vendor input from user we print the trans
                 */
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found){
            System.out.println("No transactions for " + vendor + " found.");
        }

    }

}
