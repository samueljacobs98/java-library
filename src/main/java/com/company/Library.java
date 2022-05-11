package com.company;

import java.io.*;
//import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Library {

    private final List<Book> inventory = new ArrayList<>();
    private final List<Account> accounts = new ArrayList<>();
    private final Scanner scanner;

    public Library() {
        this.scanner = new Scanner(System.in);
    }

    // Methods
    // Convert CSV data to ArrayList of Books
    public void csvToArray() {
        String file = "src/main/java/com/company/data-in/books_data.csv";
        BufferedReader reader = null;
        String line;

        try {
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null) {
                String[] row = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String title = "";
                String author = "";
                String genre = "";
                String subgenre = "";
                String publisher = "";

                for (int i = 0; i < row.length; i++) {
                    switch (i) {
                        case 1:
                            title = row[i];
                            break;
                        case 2:
                            author = row[i];
                            break;
                        case 3:
                            genre = row[i];
                            break;
                        case 4:
                            subgenre = row[i];
                            break;
                        case 5:
                            publisher = row[i];
                            break;
                    }
                }
                // Create new book
                Book newBook = new Book(title, author, genre, subgenre, publisher);
                // Add at least 1 copy
                newBook.addNewCopy();
                // Add the book to the inventory
                inventory.add(newBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Initialise the inventory with data from the csv
    public void initialiseInventory() {
        csvToArray();
        System.out.println("62: " + "Inventory initialised");
    }

    // Enquire for User Role
    public String getRole() {
        String role = "";
        List<String> roles = List.of("librarian", "member");
        boolean proceed = false;
        while (!roles.contains(role) || !proceed) {
            System.out.println("Would you like to proceed as a Librarian or a Member?");
            role = scanner.nextLine().toLowerCase(Locale.ROOT);
            System.out.println("You have entered " + role + ". Is that correct? (y/n)");
            String checker = scanner.nextLine();
            proceed = checker.equalsIgnoreCase("y");
        }
        return role;
    }

    private void loanBook(Member user) {
        System.out.println("Loan a book");
        System.out.println("What is the name of the book you would like to loan?");
        String desiredBookTitle = scanner.nextLine().toLowerCase(Locale.ROOT);
        // get the book from the array list
        Book bookToLoan = null;
        for (Book book : inventory) {
            if (book.getTitle().equalsIgnoreCase(desiredBookTitle)) {
                bookToLoan = book;
                break;
            }
        }
        if (bookToLoan == null) {
            System.out.println("Sorry, this book isn't in our inventory.");
            return;
        }
        // check if any copies are available
        List<BookItem> availableCopies = bookToLoan.getAvailableCopies();
        // if unavailable, tell user
        int numberOfAvailableCopies = availableCopies.size();
        if (numberOfAvailableCopies == 0) {
            System.out.println("Sorry, there are no copies currently available!");
            return;
        }
        // confirm number of available copies
        if (numberOfAvailableCopies == 1) {
            System.out.println("We have " + numberOfAvailableCopies + " copy available.");
        } else {
            System.out.println("We have " + numberOfAvailableCopies + " copies available.");
        }
        // if available, create new loan for book and update availability
        BookItem bookCopyToLoan = availableCopies.get(0);
        Loan newLoan = new Loan(bookCopyToLoan, user);
        bookCopyToLoan.addNewLoan(newLoan);
        ((Member) user).addToCurrentLoans(newLoan);
        // confirmation
        System.out.println("Your book has been loaned. Please return within 2 weeks.");
    }

    private void returnBook(Account user) {
        System.out.println("return a book");
        // Get current loans
        List<Loan> currentLoans = ((Member) user).getCurrentLoans();
        // What book do you want to return?
        System.out.println("What book do you want to return?");
        currentLoans.forEach(loan -> System.out.println(loan.getTitle()));

        String bookToReturn = scanner.nextLine();
        System.out.println("You would like to return " + bookToReturn + ".");
        // check if bookToReturn matches any books currently loans
        String matchItemID = "";
        String matchBookID = "";
        Loan currentLoan = null;

        for (Loan loan : currentLoans) {
            if (bookToReturn.equalsIgnoreCase(loan.getTitle())) {
                // if there is a match, get the bookID and itemID
                System.out.println("There was a match!");
                matchItemID = loan.getItemID();
                matchBookID = loan.getBookID();
                currentLoan = loan;
                break;
            }
        }

        if (matchBookID.length() == 0 && matchItemID.length() == 0) {
            System.out.println("You have not loaned this book so it cannot be returned.");
            return;
        }

        // get the book to return
        Book loanedBook = null;
        BookItem loanedBookItem = null;

        for (Book book : inventory) {
            if (book.getBookID().equalsIgnoreCase(matchBookID)) {
                loanedBook = book;
            }
        }

        if (loanedBook == null) {
            System.out.println("Sorry, there was a problem. Please try again. (1)");
            return;
        }

        for (BookItem bookItem : loanedBook.getUnavailableCopies()) {
            if (matchItemID.equalsIgnoreCase(bookItem.getItemID())) {
                loanedBookItem = bookItem;
            }
        }

        if (loanedBookItem == null) {
            System.out.println("Sorry, there was a problem. Please try again. (2)");
            return;
        }

        // update isAvailable to true
        loanedBookItem.updateAvailability();

        // update returned to true
        currentLoan.returnBook();

        // remove the loan from the user's current loans
        // add the loan to the user's previous loans
        ((Member) user).addToPreviousLoans(currentLoan);

        // provide validation for user
        currentLoan.soutDetails();
    }

    private void checkMemberLoans(Account user) {
        System.out.println("check your current loans");
        ((Member) user).getCurrentLoans().forEach(Loan::soutDetails);
    }

    // Member Application
    public void runMemberApplication() {
        System.out.println("Hi! Let's create your membership.");
        Account user = createAccount(Accounts.MEMBER);
        accounts.add(user);

        boolean proceed = false;
        String task;

        while (!proceed) {
            System.out.println("Please select a task from the following options (1/2/3):\n1. Loan a book.\n2. Return a book\n3. Check your current loans");
            task = scanner.nextLine();
            System.out.println("Task selected: " + task);
            switch (task) {
                case "1":
                    // Loan a book
                    loanBook((Member) user);
                    break;
                case "2":
                    // Return a book
                    returnBook(user);
                    break;
                case "3":
                    // Check your current loans
                    checkMemberLoans(user);
                    break;
                default:
                    System.out.println("Sorry. " + task + " is not a valid option.");
                    break;
            }
            System.out.println("Would you like to complete any other tasks today? (y/n)");
            String finalResponse = scanner.nextLine();
            proceed = finalResponse.equalsIgnoreCase("n");
        }

        System.out.println("Press enter to exit.");
        String restart = scanner.nextLine();
        if ("restart".equalsIgnoreCase(restart)) runLibraryApplication(false);
    }

    private void generateAllBooksReport() throws FileNotFoundException {
        // Generate a report of all books currently out on loan.
        List<BookItem> loanedBookItemList = new ArrayList<>();
        // For each book in the inventory
        for (Book book : inventory) {
            // If the book is unavailable
            if (book.getUnavailableCopies().size() > 0) {
                // add to the list of unavailable
                loanedBookItemList.addAll(book.getUnavailableCopies());
            }
        }

        // Create file name
        String today = LocalDate.now().toString();
        String fileName = "src/main/java/com/company/data-out/booksOnLoan" + today + ".csv";
        // Create File
        File reportCSV = new File(fileName);
        // Create File Writer
        PrintWriter printWriter = new PrintWriter(reportCSV);
        // Write column headings
        String headings = "title,author,genre,subgenre,publisher,book id,book item id,book return date\n";
        printWriter.print(headings);
        // Generate CSV report from loanedBookItemList
        for (BookItem bookItem : loanedBookItemList) {
            List<String> bookData = List.of(bookItem.getTitle(), bookItem.getAuthor(), bookItem.getGenre(), bookItem.getSubGenre(), bookItem.getPublisher(), bookItem.getBookID(), bookItem.getItemID(), bookItem.getRecentLoan().getEndDate().toString(), "\n");
            String info = String.join(",",bookData);
            printWriter.print(info);
        }
        // Generate JSON report
        Converter.bookItemsToJSON(loanedBookItemList);
        printWriter.close();
    }

    private void generateBookLoanReport() throws FileNotFoundException {
        // Generate a report of how many times a book has been loaned out
        // Get the book
        System.out.println("What book would you like to generate a report on?\n(enter the title)");
        String bookTitle = scanner.nextLine();
        Book book = null;

        for (Book ibook : inventory) {
            if (ibook.getTitle().equalsIgnoreCase(bookTitle)) {
                book = ibook;
            }
        }

        if (book == null) {
            System.out.println("Sorry, there was a problem. Please try again. (3)");
            return;
        }
        // Get each bookItem
        List<BookItem> bookCopies = book.getCopies();
        // Get the loan history of each book and add to one arrayList
        List<Loan> loanList = new ArrayList<>();
        for (BookItem bookItem : bookCopies) {
            List<Loan> loanHistory = bookItem.getLoanHistory();
            if (loanHistory.size() != 0) {
                loanList.addAll(loanHistory);
            }
        }
        // Create file name
        String today = LocalDate.now().toString();
        String fileName = "src/main/java/com/company/data-out/" + bookTitle + "LoanReport" + today + ".csv";
        fileName = fileName.replace(" ", "-");
        // Create File
        File reportCSV = new File(fileName);
        // Create File Writer
        PrintWriter printWriter = new PrintWriter(reportCSV);
        // Write column headings and total
        String total = "Total loans " + loanList.size() + "\n";
        String headings = "book ID,item ID,title,start date,end date,member ID,returned\n";

        printWriter.print(total);
        printWriter.print(headings);

        // Generate report
        for (Loan loan : loanList) {
            List<String> loanData = List.of(loan.getBookID(), loan.getItemID(), loan.getTitle(), loan.getStartDate().toString(), loan.getEndDate().toString(), loan.getMember().getiD(), loan.getReturned().toString(), "\n");
            String info = String.join(",",loanData);
            printWriter.print(info);
            Converter.loanToJSON(loan);
        }

        printWriter.close();
    }

    // Librarian Application
    public void runLibrarianApplication() {
        System.out.println("Hi! Let's create your librarian account.");
        Account user = createAccount(Accounts.LIBRARIAN);
        accounts.add(user);

        boolean proceed = false;
        String report;

        while (!proceed) {
            System.out.println("Please select the report you want to generate from the following options (1/2):\n1. All books currently out on loan.\n2. How many times a particular book has been loaned out");
            report = scanner.nextLine();
            System.out.println("Report selected: " + report);
            // Generate report
            switch (report) {
                case "1":
                    // of all books currently out on loan
                    try {
                        generateAllBooksReport();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    // Return a book
                    try {
                        generateBookLoanReport();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case "restart": // hidden option
                    runLibraryApplication(false);
                    break;
                default:
                    System.out.println("Sorry. " + report + " is not a valid option.");
                    break;
            }
            System.out.println("Would you like to complete any other tasks today? (y/n)");
            String finalResponse = scanner.nextLine();
            proceed = finalResponse.equalsIgnoreCase("n");
        }

        System.out.println("Press enter to exit.");
        String restart = scanner.nextLine();
        if ("restart".equalsIgnoreCase(restart)) runLibraryApplication(false);
    }


    // Library Application
    public void runLibraryApplication(boolean initialise) {
        if (initialise) initialiseInventory();
        String role = getRole();
        if (role.equals("member")) runMemberApplication();
        else runLibrarianApplication();
    }

    // Accounts
    public Account createAccount(Accounts accountType) {
        System.out.println("To create a new account, please provide your personal details.");
        String name = "";
        boolean proceed = false;
        while (name.length() == 0  || !proceed) {
            System.out.println("Please enter your name.");
            name = scanner.nextLine().toLowerCase(Locale.ROOT);
            System.out.println("You have entered " + name + ". Is that correct? (y/n)");
            String checker = scanner.nextLine();
            proceed = checker.equalsIgnoreCase("y");
        }
        return accountType == Accounts.MEMBER ? new Member(name) : new Librarian(name);
    }
}
