// src/main/java/com/backend/backend/service/impl/Vendor.java
package com.backend.backend.service.impl;

import com.backend.backend.dto.TicketUpdate;
import com.backend.backend.service.WebSocketService;

public class Vendor implements Runnable {

    private final TicketPool passTicket;
    private final int ticketReleaseRate;
    private int totalTickets;
    private final int maxTicket;
    private final WebSocketService webSocketService;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int totalTickets, int maxTicket, WebSocketService webSocketService) {
        this.passTicket = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
        this.maxTicket = maxTicket;
        this.webSocketService = webSocketService;
    }

    @Override
    public void run() {
        int count1 = 0;
        int x = 0;

        for (int j = maxTicket; j > 0; j -= totalTickets) {

            if (j >= totalTickets) {
                String msg1 = totalTickets + " tickets added to the Total ticket pool.";
                String msg2 = "Now total ticket value: " + passTicket.getUpdateTicket();
                String msg3 = "Remaining Max ticket value: " + (maxTicket - (totalTickets * x));
                webSocketService.sendMessage(msg1);
                webSocketService.sendMessage(msg2);
                webSocketService.sendMessage(msg3);
                x = ++count1;
            } else {
                String remaining = (maxTicket - (totalTickets * x)) + " tickets remaining to add to the Total ticket pool.";
                String addRemain = "Added remaining total ticket value: " + (maxTicket - (totalTickets * x));
                totalTickets = maxTicket - (totalTickets * x);
                String remainingMax = "Remaining Max ticket value: " + (maxTicket - ((totalTickets * x) + (maxTicket - (totalTickets * x))));
                webSocketService.sendMessage(remaining);
                webSocketService.sendMessage(addRemain);
                webSocketService.sendMessage(remainingMax);
            }

            webSocketService.sendMessage(""); // For spacing

            int y = 0;
            int count2 = 0;
            for (int i = totalTickets; i > 0; i -= ticketReleaseRate) {
                if (i >= ticketReleaseRate) {
                    passTicket.addTickets(ticketReleaseRate);
                    y = ++count2;
                } else {
                    passTicket.addTickets(totalTickets - ticketReleaseRate * y);
                }

                // Send the updated ticket data
                TicketUpdate update = new TicketUpdate(
                        passTicket.getUpdateTicket(),
                        ticketReleaseRate,
                        passTicket.getCustomerRetrievalRate(),
                        maxTicket
                );
                webSocketService.sendTicketUpdate(update);

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    webSocketService.sendMessage("Vendor thread interrupted.");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
