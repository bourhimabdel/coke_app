package com.cokeappingo.SendNotificationPack;

public class Data {
    private String id_push;
    private String date;
    private String status;
    private String cause_refuse;

    public String getCause_refuse() {
        return cause_refuse;
    }

    public void setCause_refuse(String cause_refuse) {
        this.cause_refuse = cause_refuse;
    }

    public Data(String id_push, String date, String status) {
        this.id_push = id_push;
        this.date = date;
        this.status = status;
    }

    public String getId_push() {
        return id_push;
    }

    public void setId_push(String id_push) {
        this.id_push = id_push;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
