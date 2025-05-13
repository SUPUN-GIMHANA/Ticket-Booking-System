package com.backend.backend.service.impl;

import com.backend.backend.service.WebSocketService;

public class CustomerCLI implements Runnable {

    private final TicketPool getTicketHandel;
    private final int maxValue;
    private final WebSocketService webSocketService;

    public CustomerCLI(TicketPool ticket, int maxValue, WebSocketService webSocketService) {
        this.getTicketHandel = ticket;
        this.maxValue = maxValue;
        this.webSocketService = webSocketService;
    }

    @Override
    public void run() {
        for (int i = 0; i < maxValue; i++) {
            int removedTickets = getTicketHandel.removeTicket();

            // Example message after removing a ticket
            webSocketService.sendMessage("Customer purchased " + removedTickets + " tickets. Current total: " + getTicketHandel.getUpdateTicket());

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                webSocketService.sendMessage("CustomerCLI thread interrupted.");
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
