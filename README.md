# Java Library Project

## Project Overview

The goal of this project was to build a library system with a command line interface in Java where data can be loaded in and exported out.

## Design approach

On initialisation the library class imports book data from a CSV file and uses it to populate an inventory of books. Each book has at least one book item; this enables the library to host multiple copies of the same book.

A loan class was used to gather details about each book loan, including the ID of the member loaning the book, the loan-out date and the return-by date, the book ID and book item ID. Each loan was stored in a loan history array list for each book item.

A requirement of the system was that a librarian account could export data about book loan history. This was implemented and all data is exported into the data-out directory.

Another required feature was library members should be able to loan a book from the inventory, check current loan details, and return a loaned book.

## Links

### GitHub Repo

https://github.com/samueljacobs98/java-library
# java-library
