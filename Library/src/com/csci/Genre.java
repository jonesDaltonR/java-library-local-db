package com.csci;

public class Genre {
    private int _id;
    private String _name;

    public Genre(){}

    public Genre(int _id, String _name) {
        this._id = _id;
        this._name = _name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getNme() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    @Override
    public String toString() {
        return _name.replace('_',' ');
    }
}
