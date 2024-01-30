package assignment2.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import java.util.concurrent.Flow;
import java.util.stream.Collectors;

import assignment2.database.Database;
import assignment2.models.Book;

public class BookStoreGUI extends JFrame {
    private JTextField bookIdField, bookNameField, authorNamesField, publicationField, dateOfPublicationField,
            priceOfBookField, totalQuantityField, searchField;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTable table;
    private JLabel bookIdLabel,bookNameLabel,authorNameLabel,publicationLabel,dateOfPublicationLabel,priceOfBookLabel,totalQuantityLabel; 

    BookTableModel tableModel;
    Database db;

    Book bookToEdit;

    public BookStoreGUI(Database db) {
        this.db = db;
        tableModel = new BookTableModel(db.booksCollection);

        setTitle("Book Store");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        // setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createUI();
    }

    private void createUI() {
        createBookPanelGUI();
        readBookGUI();
        add(tabbedPane);
    }

    private void createBookPanelGUI() {
        JPanel createBookPanel = new JPanel(new GridLayout(8,2,10,10));

        bookIdLabel = new JLabel("Book ID:"); 
        bookIdLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(bookIdLabel);
        bookIdField = new JTextField();
        bookIdField.setFont(getFont().deriveFont(Font.PLAIN, 25));
        createBookPanel.add(bookIdField);

        bookNameLabel = new JLabel("Book Name:"); 
        bookNameLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(bookNameLabel);
        bookNameField = new JTextField();
        bookNameField.setFont(getFont().deriveFont(Font.PLAIN, 25));
        createBookPanel.add(bookNameField);
//
        authorNameLabel = new JLabel("Author Names:"); 
        authorNameLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(authorNameLabel);
        authorNamesField = new JTextField();
        authorNamesField.setFont(getFont().deriveFont(Font.PLAIN, 25));
        createBookPanel.add(authorNamesField);

        publicationLabel = new JLabel("Publication:"); 
        publicationLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(publicationLabel);
        publicationField = new JTextField();
        publicationField.setFont(getFont().deriveFont(Font.PLAIN, 25));
        createBookPanel.add(publicationField);

        dateOfPublicationLabel = new JLabel("Date of Publication:"); 
        dateOfPublicationLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(dateOfPublicationLabel);
        dateOfPublicationField = new JTextField();
        dateOfPublicationField.setFont(getFont().deriveFont(Font.PLAIN,25));
        createBookPanel.add(dateOfPublicationField);

        priceOfBookLabel = new JLabel("Price of Book:"); 
        priceOfBookLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(priceOfBookLabel);
        priceOfBookField = new JTextField();
        priceOfBookField.setFont(getFont().deriveFont(Font.PLAIN, 25));
        createBookPanel.add(priceOfBookField);

        totalQuantityLabel = new JLabel("Total Quantity to Order:"); 
        totalQuantityLabel.setFont(getFont().deriveFont(Font.BOLD,25));
        createBookPanel.add(totalQuantityLabel);
        totalQuantityField = new JTextField();
        totalQuantityField.setFont(getFont().deriveFont(Font.PLAIN, 25));
        createBookPanel.add(totalQuantityField);

        JButton saveButton = new JButton("Save");
        saveButton.setBackground(Color.GREEN);
        saveButton.setFont(getFont().deriveFont(Font.BOLD,25));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Book newBook = new Book(
                        Integer.parseInt(bookIdField.getText()),
                        bookNameField.getText(),
                        authorNamesField.getText(),
                        publicationField.getText(),
                        dateOfPublicationField.getText(),
                        Double.parseDouble(priceOfBookField.getText()),
                        Integer.parseInt(totalQuantityField.getText()));

                if (bookToEdit != null) {
                    db.updateBook(table.getSelectedRow(), newBook);
                    showDialog("Book updated successfully!");
                } else {
                    db.createBook(newBook);
                    showDialog("Book saved successfully!");
                }
                clearInputFields();
                tableModel.setData(db.booksCollection);
            }
        });

        createBookPanel.add(saveButton);
        tabbedPane.addTab("Create Book", createBookPanel);
        tabbedPane.setBackground(Color.CYAN);
    }

    private void readBookGUI() {
        JPanel displayPanel = new JPanel(new GridLayout(5,2,10,10));

        searchField = new JTextField("Search for books 🔎");
        searchField.setFont(searchField.getFont().deriveFont(Font.PLAIN, 35));
        displayPanel.add(searchField);
        
        JButton searchButton = new JButton("Search");
        searchButton.setFont(getFont().deriveFont(Font.BOLD,25));
        searchButton.setBackground(Color.LIGHT_GRAY);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchString = searchField.getText().toLowerCase();
                java.util.List<Book> filteredBooksCollection = db.booksCollection.stream()
                        .filter(book -> book.matchesSearch(searchString)).collect(Collectors.toList());
                tableModel.setData(filteredBooksCollection);
            }
        });
        displayPanel.add(searchButton);

        table = new JTable(tableModel);
        table.setSize(100, 100);
        table.setFont(getFont().deriveFont(Font.PLAIN,19));
        table.setRowHeight(70);
        JScrollPane tableScrollPane = new JScrollPane(table);
        displayPanel.add(tableScrollPane);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(getFont().deriveFont(Font.BOLD,25));
        deleteButton.setBackground(Color.RED);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                if (selectedRowIndex == -1) {
                    showDialog("Please select a row to delete");
                    return;
                }

                Book bookToBeDeleted = db.booksCollection.get(selectedRowIndex);
                db.deleteBook(selectedRowIndex);
                showDialog(bookToBeDeleted.bookName + " successfully deleted.");
                tableModel.setData(db.booksCollection);
            }
        });
        displayPanel.add(deleteButton);


        JButton editButton = new JButton("Edit");
        editButton.setFont(getFont().deriveFont(Font.BOLD,25));
        editButton.setBackground(Color.BLUE);
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() == -1) {
                    showDialog("Please select a row to edit");
                    return;
                }
                bookToEdit = db.booksCollection.get((table.getSelectedRow()));
                tabbedPane.setSelectedIndex(0);
                bookIdField.setText(String.valueOf(bookToEdit.bookId));
                bookIdField.setEditable(false);
                bookNameField.setText(bookToEdit.bookName);
                authorNamesField.setText(bookToEdit.authorNames);
                publicationField.setText(bookToEdit.publication);
                dateOfPublicationField.setText(bookToEdit.dateOfPublication);
                priceOfBookField.setText(String.valueOf(bookToEdit.priceOfBook));
                totalQuantityField.setText(String.valueOf(bookToEdit.totalQuantityToOrder));
            }
        });
        displayPanel.add(editButton);

        tabbedPane.addTab("Display Books", displayPanel);
        tabbedPane.setBackground(Color.CYAN);
    }

    private void showDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private void clearInputFields() {
        bookIdField.setText("");
        bookNameField.setText("");
        authorNamesField.setText("");
        publicationField.setText("");
        dateOfPublicationField.setText("");
        priceOfBookField.setText("");
        totalQuantityField.setText("");
        bookToEdit = null;
    }
}
