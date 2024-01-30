package assignment2.database;

import java.util.*;
import java.sql.*;
import java.text.*;

import assignment2.models.Book;

public class Database {
    public ArrayList<Book> booksCollection = new ArrayList<Book>();
    Connection con;

    static String jdbcUrl = "jdbc:mysql://localhost:3306/assignment2";
    static String username = "root";
    static String password = "1030";

    public Database() {
        connectDB();
        readBooks();
    }

    private void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(jdbcUrl, username, password);
            if (con == null) throw new Exception("Could not connect to database");
            System.out.println("DB connection successful!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void saveBooksCollection() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sql1 = "TRUNCATE books;";
            Statement statement = con.createStatement();
            String storedProcedureName = "updateBookPrices";
            statement.execute(sql1);
            statement.close();

            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO books VALUES (?, ?, ?, ?, ?, ?, ?)");            
            for (Book b : booksCollection) {
                preparedStatement.setInt(1, b.bookId);
                preparedStatement.setString(2, b.bookName);
                preparedStatement.setString(3, b.authorNames);
                preparedStatement.setString(4, b.publication);
                preparedStatement.setString(5, b.dateOfPublication);
                preparedStatement.setDouble(6, b.priceOfBook);
                preparedStatement.setInt(7, b.totalQuantityToOrder);

                preparedStatement.executeUpdate();
            }

            preparedStatement.close();

            CallableStatement callableStatement = con.prepareCall("{call " + storedProcedureName + "}");
            callableStatement.execute();
            callableStatement.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void readBooks() {
        try {
            String query = "SELECT * FROM books;";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Book b = new Book(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("authors"),
                    rs.getString("publication"),
                    rs.getString("dateOfPublication"),
                    rs.getDouble("price"),
                    rs.getInt("quantity")
                );

                booksCollection.add(b);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void createBook(Book newBook) {
        booksCollection.add(newBook);
        saveBooksCollection();
    }

    public void deleteBook(int index) {
        booksCollection.remove(index);
        saveBooksCollection();
    }

    public void updateBook(int index, Book book) {
        booksCollection.set(index, book);
        saveBooksCollection();
    }
}
