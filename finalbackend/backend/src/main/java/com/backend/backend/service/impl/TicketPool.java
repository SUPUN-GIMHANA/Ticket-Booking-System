package com.backend.backend.service.impl;

public class TicketPool {

    private int totalTickets;
    private boolean available;
    private final int customerRetrievalRate;
    private final int addReleaseTicket;
    private int updateTicket = 0;
    private final int maxTicket;

    public TicketPool(int totalTickets, int customerRetrievalRate, int addReleaseTicket, int maxTicket) {
        this.totalTickets = totalTickets;
        this.customerRetrievalRate = customerRetrievalRate;
        this.addReleaseTicket = addReleaseTicket;
        this.maxTicket = maxTicket;
    }

    synchronized public void addTickets(int value) {
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        updateTicket += value; // Update the total ticket count
        available = true;
        // Note: Messages are now handled in Vendor.java via WebSocketService
        notifyAll();
    }

    synchronized public int removeTicket() {
        while (updateTicket < customerRetrievalRate) { // Wait if there are fewer than retrieval rate tickets
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return 0;
            }
        }
        updateTicket -= customerRetrievalRate; // Reduce tickets based on retrieval rate
        available = false;
        notify();
        return customerRetrievalRate;
    }

    public int getUpdateTicket() {
        return updateTicket;
    }


    public int getCustomerRetrievalRate() {
        return 0;
    }
}
