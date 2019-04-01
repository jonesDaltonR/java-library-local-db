package com.csci;

public class Author {

    private String _name;
    private int _id;

    public Author() {}
    public Author(String name,int id)
    {
        _name = name;
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return _name;
    }
}
