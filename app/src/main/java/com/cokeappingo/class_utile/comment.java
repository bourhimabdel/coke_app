package com.cokeappingo.class_utile;

public class comment {

    String id_visitor;
    int value;
    String date;
    String comment;


    public comment(String id_visitor, int value, String date, String comment) {
        this.id_visitor = id_visitor;
        this.value = value;
        this.date = date;
        this.comment = comment;
    }

    public int getValue() {
        return value;
    }

    public comment(){}

    public void setValue(int value) {
        this.value = value;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId_visitor() {
        return id_visitor;
    }

    public void setId_visitor(String id_visitor) {
        this.id_visitor = id_visitor;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
