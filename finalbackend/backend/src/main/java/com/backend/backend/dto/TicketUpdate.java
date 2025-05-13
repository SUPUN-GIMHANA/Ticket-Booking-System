

// src/main/java/com/backend/backend/dto/TicketUpdate.java
package com.backend.backend.dto;

public class TicketUpdate {
    private int totalTickets; // Plural to match frontend
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maximumNumberOfTickets;

    // Constructors
    public TicketUpdate() {}

    public TicketUpdate(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maximumNumberOfTickets) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maximumNumberOfTickets = maximumNumberOfTickets;
    }

    // Getters and Setters
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaximumNumberOfTickets() {
        return maximumNumberOfTickets;
    }

    public void setMaximumNumberOfTickets(int maximumNumberOfTickets) {
        this.maximumNumberOfTickets = maximumNumberOfTickets;
    }
}
