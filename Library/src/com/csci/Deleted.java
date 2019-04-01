package com.csci;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Deleted implements Initializable {

    public Button btnSearchButton;
    public ListView<Book> lstSearchResults;
    public Button btnUndelete;
    public Button btnPurge;
    public ComboBox<Genre> ddlGenre;
    public TextField txtTitle;
    public TextField txtAuthor;
    public TextField txtISBN;
    public TextField txtSearchField;
    public Button btnResetList;
    private long storedISBN;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Start();

        btnSearchButton.setOnMouseClicked(event -> Search());
        btnPurge.setOnMouseClicked(event -> Purge());
        btnUndelete.setOnMouseClicked(event -> Undelete());
        btnResetList.setOnMouseClicked(event -> Reset());

        txtISBN.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                char ch = txtISBN.getText().charAt(oldValue.intValue());
                // Check if the new character is the number or other's
                if (!(ch >= '0' && ch <= '9' )) {
                    // if it's not number then just setText to previous one
                    txtISBN.setText(txtISBN.getText().substring(0,txtISBN.getText().length()-1));
                }
            }
        });
        txtISBN.textProperty().addListener((observable, oldValue, newValue) -> {
            if(txtISBN.getLength() > 13) {
                String s = txtISBN.getText().substring(0, 13);
                txtISBN.setText(s);
            }
        });

        lstSearchResults.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(observable.getValue() != null)
            {
                Book selectedBook = observable.getValue();
                storedISBN = selectedBook.getIsbn();
                txtISBN.setText(String.valueOf(storedISBN));
                txtTitle.setText(selectedBook.getTitle());
                txtAuthor.setText(selectedBook.getAuthor());
                ddlGenre.getSelectionModel().select(selectedBook.getGenre()-1);
            }
            else
                Clear();
        });
    }

    private void Reset()
    {
        db database = new db();

        try {
            storedISBN = 0;
            lstSearchResults.getSelectionModel().clearSelection();
            List<Book> results = database.GetAllBooks("D");
            LoadList(results);
        }catch (SQLException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "Something went wrong with connecting to the database","Database Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }


    private void Purge()
    {
        try
        {
            db db = new db();
            long isbn = Long.parseLong(txtISBN.getText());
            int exist = db.CheckForBook(isbn);

            switch (exist)
            {
                case 0:
                    JOptionPane.showMessageDialog(null, "No book in database with matching ISBN","Results",JOptionPane.PLAIN_MESSAGE);
                    break;
                case 1:
                    int results = db.PurgeBook(isbn);
                    JOptionPane.showMessageDialog(null, String.format("%d rows purged",results),"Results",JOptionPane.PLAIN_MESSAGE);
                    Reset();
                    Clear();
                    break;
                case 2:
                    JOptionPane.showMessageDialog(null, "Something went wrong with purging a record from the database","Database Error",JOptionPane.ERROR_MESSAGE);
                    break;
            }

        }catch (ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Something went wrong with updating or saving the book to the database","Database Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    private void Undelete()
    {
        try
        {
            Book newBook = new Book();
            newBook.setTitle(txtTitle.getText());
            newBook.setAuthor(txtAuthor.getText());
            newBook.setIsbn(Long.parseLong(txtISBN.getText()));
            newBook.setGenre(ddlGenre.getValue().getId());
            db database = new db();
            int result = database.UndeleteBook(storedISBN);

            switch (result)
            {
                case 0:
                    JOptionPane.showMessageDialog(null, "No rows updated","Results",JOptionPane.PLAIN_MESSAGE);
                    break;
                case -1:
                    JOptionPane.showMessageDialog(null, "Something went wrong with updating the database","Error",JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, String.format("%d rows updated",result),"Results",JOptionPane.PLAIN_MESSAGE);
                    Reset();
                    Clear();
                    break;
            }

        }catch (ClassNotFoundException | SQLException e)
        {
            JOptionPane.showMessageDialog(null, "Something went wrong with updating or saving the book to the database","Database Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    private void Search()
    {
        String search = txtSearchField.getText();

        if(!search.equals(""))
        {
            db database = new db();

            try {
                List<Book> results = database.SearchBooks(search,"A");
                LoadList(results);

            }catch (SQLException | ClassNotFoundException e)
            {
                JOptionPane.showMessageDialog(null, "Something went wrong with connecting to the database","Database Error",JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }

        }
    }

    private void Start()
    {
        db database = new db();

        try {
            List<Book> results = database.GetAllBooks("D");
            LoadList(results);

            List<Genre> genre = database.GetGenres();
            ddlGenre.getItems().addAll(genre);

        }catch (SQLException | ClassNotFoundException e)
        {
            JOptionPane.showMessageDialog(null, "Something went wrong with connecting to the database","Database Error",JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void Clear()
    {
        txtAuthor.clear();
        txtISBN.clear();
        txtTitle.clear();
        ddlGenre.getSelectionModel().clearSelection();
    }

    private void LoadList(List<Book> books)
    {
        lstSearchResults.getItems().clear();
        lstSearchResults.getItems().addAll(books);
    }
}
