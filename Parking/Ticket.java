package com.company;

public class Ticket {
    private int id;
    private boolean isFree;

    public Ticket(int id) {
        this.id = id;
        isFree = true;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return isFree;
    }

    public void setAvailability(boolean free) {
        isFree = free;
    }
}
