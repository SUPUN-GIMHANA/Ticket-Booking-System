// src/main/java/com/backend/backend/service/WebSocketService.java
package com.backend.backend.service;

import com.backend.backend.dto.TicketUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Sends a TicketUpdate object to the frontend subscribed to /topic/ticketUpdates
     *
     * @param update The TicketUpdate object containing the latest ticket data
     */
    public void sendTicketUpdate(TicketUpdate update) {
        messagingTemplate.convertAndSend("/topic/ticketUpdates", update);
    }

    /**
     * Sends a plain message to the frontend subscribed to /topic/messages
     *
     * @param message The message to send
     */
    public void sendMessage(String message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}
