package com.backend.backend.service.impl;

import com.backend.backend.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailApp {

    private final WebSocketService webSocketService;

    @Autowired
    public MailApp(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    public void runApp(int totalTicket, int ticketReleaseRate, int customerRetrievalRate, int maximumNumberOfTickets) {
        TicketPool ticketPool = new TicketPool(totalTicket, customerRetrievalRate, ticketReleaseRate, maximumNumberOfTickets);

        com.backend.backend.service.impl.Vendor vendor = new com.backend.backend.service.impl.Vendor(ticketPool, ticketReleaseRate, totalTicket, maximumNumberOfTickets, webSocketService);
        CustomerCLI customer = new CustomerCLI(ticketPool, maximumNumberOfTickets, webSocketService);

        Thread vendorThread = new Thread(vendor);
        Thread customerThread = new Thread(customer);

        vendorThread.start();
        customerThread.start();
    }
}
