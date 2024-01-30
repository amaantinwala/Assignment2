package assignment2.models;

import java.io.Serializable;

public class Book implements Serializable {
    public int bookId;
    public String bookName;
    public String authorNames;
    public String publication;
    public String dateOfPublication;
    public double priceOfBook;
    public int totalQuantityToOrder;
    public double totalCost;
    private static final long serialVersionUID = 6529685098267757690L;

    public Book() {
    }

    public Book(int bookId, String bookName, String authorNames, String publication, String dateOfPublication,
            double priceOfBook, int totalQuantityToOrder) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorNames = authorNames;
        this.publication = publication;
        this.dateOfPublication = dateOfPublication;
        this.priceOfBook = priceOfBook;
        this.totalQuantityToOrder = totalQuantityToOrder;
        this.totalCost = priceOfBook * totalQuantityToOrder;
    }

    public boolean matchesSearch(String query) {
        if (bookName.toLowerCase().contains(query)||String.valueOf(bookId).contains(query)||authorNames.toLowerCase().contains(query)||publication.toLowerCase().contains(query))
            return true;

        return false;
    }
}
