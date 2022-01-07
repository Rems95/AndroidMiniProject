package com.example.myapplication;


public class Trajet {
            private String unix;
            private String from;
            private String to;
            private String name;
            private String seat;

    public Trajet() {

    }

    public String getUnix() {
        return unix;
    }

    public void setUnix(String unix) {
        this.unix = unix;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "unix='" + unix + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", name='" + name + '\'' +
                ", seat='" + seat + '\'' +
                '}';
    }
}
