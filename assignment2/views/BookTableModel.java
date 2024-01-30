package assignment2.views;

import java.util.List;
import javax.swing.table.*;

import assignment2.models.Book;

public class BookTableModel extends AbstractTableModel {
    private List<Book> data;
    private String[] columnNames = { "ID", "Name", "Authors", "Publication", "Publication Date", "Price", "Quantity",
            "Total Cost"};

    public void setData(List<Book> data) {
        this.data = data;
        fireTableChanged(null);
    }

    public BookTableModel(List<Book> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = data.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return String.valueOf(book.bookId);
            case 1:
                return book.bookName;
            case 2:
                return book.authorNames;
            case 3:
                return book.publication;
            case 4:
                return book.dateOfPublication;
            case 5:
                return book.priceOfBook;
            case 6:
                return book.totalQuantityToOrder;
            case 7:
                return book.totalCost;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}