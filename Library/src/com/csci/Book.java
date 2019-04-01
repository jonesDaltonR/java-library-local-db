package com.csci;


public class Book {
    private long _isbn;
    private String _author;
    private String _title;
    private int _genre;

    public Book(){ }
    public Book(long _isbn, String _author, String _title, int _genre) {
        this._isbn = _isbn;
        this._author = _author;
        this._title = _title;
        this._genre = _genre;
    }

    public long getIsbn() {
        return _isbn;
    }

    public void setIsbn(long _isbn) {
        this._isbn = _isbn;
    }

    public String getAuthor() {
        return _author;
    }

    public void setAuthor(String _author) {
        this._author = _author;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public int getGenre() {
        return _genre;
    }

    public void setGenre(int _genre) {
        this._genre = _genre;
    }

    @Override
    public String toString() {
        return _title;
    }
}

