package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class Converter {
    public static void loanToJSON(Loan loan) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Book_ID", loan.getBookID());
        jsonObject.put("Item_ID", loan.getItemID());
        jsonObject.put("Title", loan.getBookID());
        jsonObject.put("Start_Date", loan.getStartDate().toString());
        jsonObject.put("End_Date", loan.getEndDate().toString());
        jsonObject.put("Member_ID", loan.getMember().getiD());
        jsonObject.put("Returned", loan.getReturned());

        try {
            String today = LocalDate.now().toString();
            String fileName = "src/main/java/com/company/data-out/loans/" + loan.getTitle().replace(" ", "_") + today + ".json";
            FileWriter file = new FileWriter(fileName);
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bookItemsToJSON(List<BookItem> loanedBookItemList) {
        String today = LocalDate.now().toString();
        String fileName = "src/main/java/com/company/data-out/books-on-loan/" + today + "loans.json";
        try {
            FileWriter file = new FileWriter(fileName);
            JSONArray loanedBooksJson = new JSONArray();

            for (BookItem bookItem : loanedBookItemList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", bookItem.getTitle());
                jsonObject.put("author", bookItem.getAuthor());
                jsonObject.put("genre", bookItem.getGenre());
                jsonObject.put("subgenre", bookItem.getSubGenre());
                jsonObject.put("publisher", bookItem.getPublisher());
                jsonObject.put("book id", bookItem.getBookID());
                jsonObject.put("item id", bookItem.getItemID());
                jsonObject.put("book return date", bookItem.getRecentLoan().getEndDate().toString());
                loanedBooksJson.add(jsonObject.toJSONString());
            }
            file.write(loanedBooksJson.toJSONString());
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

