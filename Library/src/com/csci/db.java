package com.csci;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class db {
    private Connection connection = null;

    public db(){}

    public List<Book> GetAllBooks(String status) throws SQLException,ClassNotFoundException
    {
        List<Book> books = new ArrayList<>();
        String sql =
                "SELECT isbn,author.name AS \"author name\",title,genre FROM book " +
                "LEFT JOIN author ON author.auth_id = book.author WHERE status = ?;";

        GetConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1,status);
        ResultSet rs = stmt.executeQuery();
        while (rs.next())
        {
            Book book = new Book();
            book.setIsbn(rs.getLong("isbn"));
            book.setAuthor(rs.getString("author name"));
            book.setTitle(rs.getString("title"));
            book.setGenre(rs.getInt("genre"));

            books.add(book);
        }
        rs.close();
        stmt.close();
        CloseConnection();

        return books;
    }
    public List<Genre> GetGenres() throws SQLException,ClassNotFoundException
    {
        List<Genre> genres = new ArrayList<>();
        String sql =
                "SELECT * from genre;";

        GetConnection();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next())
        {
            Genre genre = new Genre();
            genre.setId(rs.getInt("gen_id"));
            genre.setName(rs.getString("name"));

            genres.add(genre);
        }
        rs.close();
        stmt.close();
        CloseConnection();
        return genres;
    }
    public List<Book> SearchBooks(String input,String status) throws SQLException,ClassNotFoundException
    {
        List<Book> books = new ArrayList<>();
        String sql =
                "SELECT isbn,author.name as \"author name\",title,genre FROM book " +
                        "LEFT JOIN author on author.auth_id = book.author "+
                        "LEFT JOIN genre on genre.gen_id = book.genre "+
                        "WHERE " +
                        "status = ? AND"+
                        "isbn LIKE ? OR " +
                        "author.name LIKE ? OR " +
                        "title LIKE ? OR " +
                        "genre.name LIKE ?;";

        GetConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1,status);
        stmt.setString(2,'%'+input+'%');
        stmt.setString(3,'%'+input+'%');
        stmt.setString(4,'%'+input+'%');
        stmt.setString(5,'%'+input+'%');

        ResultSet rs = stmt.executeQuery();
        while (rs.next())
        {
            Book book = new Book();
            book.setIsbn(rs.getLong("isbn"));
            book.setAuthor(rs.getString("author name"));
            book.setTitle(rs.getString("title"));
            book.setGenre(rs.getInt("genre"));

            books.add(book);
        }
        rs.close();
        stmt.close();
        CloseConnection();

        return books;
    }
    public int DeleteBook(long isbn)throws SQLException,ClassNotFoundException
    {
        String sql = "UPDATE book set status = \"D\", update_date = ? WHERE isbn = ?";
        GetConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, Instant.now().getEpochSecond());
        stmt.setLong(2,isbn);
        return stmt.executeUpdate();
    }
    public int UndeleteBook(long isbn) throws SQLException,ClassNotFoundException
    {
        String sql = "UPDATE book set status = \"A\", update_date = ? WHERE isbn = ?";
        GetConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1, Instant.now().getEpochSecond());
        stmt.setLong(2,isbn);
        return stmt.executeUpdate();
    }
    public int PurgeBook(long isbn) throws SQLException,ClassNotFoundException
    {
        String sql = "DELETE FROM book WHERE isbn = ?";
        GetConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setLong(1,isbn);
        return stmt.executeUpdate();
    }

    public int SaveBook(Book book,long isbn)throws SQLException,ClassNotFoundException
    {
        int exist = book.getIsbn() == isbn ? 1 : CheckForBook(book.getIsbn());

        switch (exist)
        {
            case 0:
                return AddBook(book);
            case 1:
                return UpdateBook(book,isbn);
            case 2:
            default:
                return -1;
        }
    }
    public int CheckForBook(long isbn)throws SQLException,ClassNotFoundException
    {
        String countCheck = "SELECT COUNT(*) AS \"count\" from book where isbn = ?;";
        GetConnection();
        PreparedStatement stmt = connection.prepareStatement(countCheck);
        stmt.setLong(1,isbn);
        ResultSet results = stmt.executeQuery();

        if(results.next()) {
            int res = results.getInt("count");
            CloseConnection();
            return res;
        }
        else
            return 2;
    }

    private int AddAuthor(String authorName) throws SQLException,ClassNotFoundException
    {
        GetConnection();
        String insertNewAuthor = "insert into author(name) SELECT ? FROM author EXCEPT SELECT name FROM author WHERE name =?;";

        PreparedStatement statement = connection.prepareStatement(insertNewAuthor);
        statement.setString(1,authorName);
        statement.setString(2,authorName);

        int results = statement.executeUpdate();
        CloseConnection();
        return results;
    }

    private int AddBook(Book newBook)throws SQLException,ClassNotFoundException
    {
        AddAuthor(newBook.getAuthor());
        String insertNewBook = "insert into book(isbn,author,title,genre,update_date) values (?,(SELECT auth_id from author WHERE name=?),?,?,?);";
        GetConnection();
        PreparedStatement statement = connection.prepareStatement(insertNewBook);
        statement.setLong(1,newBook.getIsbn());
        statement.setString(2,newBook.getAuthor());
        statement.setString(3,newBook.getTitle());
        statement.setInt(4,newBook.getGenre());
        statement.setLong(5, Instant.now().getEpochSecond());

        int results =statement.executeUpdate();
        CloseConnection();
        return results;
    }

    private int UpdateBook(Book newBook,long isbn)throws SQLException,ClassNotFoundException
    {
        AddAuthor(newBook.getAuthor());
        String updateCurrentBookSQL = "UPDATE book SET isbn = ?,author = (SELECT auth_id from author WHERE name=?),title = ?,genre = ?,update_date = ? WHERE isbn = ?;";

        GetConnection();
        PreparedStatement statement = connection.prepareStatement(updateCurrentBookSQL);
        statement.setLong(1,newBook.getIsbn());
        statement.setString(2,newBook.getAuthor());
        statement.setString(3,newBook.getTitle());
        statement.setInt(4,newBook.getGenre());
        statement.setLong(5, Instant.now().getEpochSecond());
        statement.setLong(6,isbn);

        int results =statement.executeUpdate();
        CloseConnection();
        return results;
    }


    private void GetConnection() throws ClassNotFoundException,SQLException
    {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:library.db");
    }
    private void CloseConnection() throws SQLException
    {
        connection.close();
        connection = null;
    }

}
