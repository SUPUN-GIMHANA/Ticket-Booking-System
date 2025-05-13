package org.example;

import java.util.ArrayList;
import java.util.List;

public class TicketPool extends Configure {

    private int totalTickets;
    private boolean available;
    private int customerRetrievalRate;
    private int addReleaseTicket;
    private int updateTicket =0;
    private int maxTicket;


    public TicketPool(int totalTickets,int customerRetrievalRate,int addReleaseTicket,int maxTicket) {
        this.totalTickets = totalTickets;
        this.customerRetrievalRate = customerRetrievalRate;
        this.addReleaseTicket = addReleaseTicket;
        this.maxTicket = maxTicket;

        List<String> maxticketArray = new ArrayList<>();
        List<String> totalTicketArray = new ArrayList<>();

    }

//    List<Ticket> saveTicket = Collections.synchronizedList(new ArrayList<>());
    synchronized public void addTickets(int value) {
        while (available) {
            try {
                wait();
//                System.out.println("wait see add to the ticket pool");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateTicket += value; // Update the total ticket count
        available = true;
        System.out.println(value + " ticket release by Vendor in total tikets");
        System.out.println("Now customer can get total tickets value: " + updateTicket);

        System.out.println();
        available=true;
        notifyAll();
    }

    synchronized public int removeTicket() {

        while (updateTicket < customerRetrievalRate) {// Wait if there are fewer than 2 tickets
            try {
                wait();
//                System.out.println("wait see to remove the ticket from the pool");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateTicket -= customerRetrievalRate; // Reduce 2 tickets for each purchase
        available = false;
        System.out.println(customerRetrievalRate +" Tickets are purchased");
        System.out.println("Total tickets: " + updateTicket);
        System.out.println();

        notify();
        return addReleaseTicket;
    }

}
