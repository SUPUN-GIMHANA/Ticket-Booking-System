package org.example;

public class Customer  implements Runnable {


    private TicketPool getTicketHandel;
    private int maxValue;

    public Customer (TicketPool ticket, int maxValue) {
        this.getTicketHandel = ticket;
        this.maxValue = maxValue;
    }

    @Override
    public void run() {

        for (int i = 0; i < maxValue ; i++) {
            getTicketHandel.removeTicket();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
