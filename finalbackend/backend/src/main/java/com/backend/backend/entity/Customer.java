package com.backend.backend.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Customer {

    @Id
    @Column(name = "customer_id", nullable = false)
    private int id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "total_ticket", nullable = false)
    private int totalTicket;

    @Column(name = "ticket_releaseRate", nullable = false)
    private int ticketReleaseRate;

    @Column(name = "customer_retrievalRate", nullable = false)
    private int customerRetrievalRate;

    @Column(name = "maximum_numberOfTickets", nullable = false)
    private int maximumNumberOfTickets;

    public Customer(int id, String customerName, int totalTicket, int ticketReleaseRate, int customerRetrievalRate, int maximumNumberOfTickets) {
        this.id = id;
        this.customerName = customerName;
        this.totalTicket = totalTicket;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maximumNumberOfTickets = maximumNumberOfTickets;
    }

    public Customer() {

    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getTotalTicket() {
        return totalTicket;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaximumNumberOfTickets() {
        return maximumNumberOfTickets;
    }
}
