package org.example;

public class MailApp {

    public static void runApp(int total_ticket,int ticketReleaseRate,int customerRetrievalRate,int maximumNumberOfTickets) {
        TicketPool ticketPool = new TicketPool(total_ticket,customerRetrievalRate,ticketReleaseRate,maximumNumberOfTickets);

//        MaxTicket maxTicket = new MaxTicket(ticketPool,maximumNumberOfTickets,total_ticket);
        Vendor Vendor = new Vendor(ticketPool,ticketReleaseRate,total_ticket,maximumNumberOfTickets);
        Customer Customer = new Customer(ticketPool,maximumNumberOfTickets);
//
//        Thread MaxTicketThread = new Thread(maxTicket);
        Thread VendorThread = new Thread(Vendor);
        Thread CustomerThread = new Thread(Customer);

//        MaxTicketThread.start();
        VendorThread.start();
        CustomerThread.start();

    }
}
