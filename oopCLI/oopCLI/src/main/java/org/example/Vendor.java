package org.example;

public class Vendor implements Runnable {

    private TicketPool passTicket;
    private int ticketReleaseRate;
    private int totalTickets;
    private int maxTicket;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate, int totalTickets, int maxTicket) {
        this.passTicket = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
        this.maxTicket = maxTicket;
    }

    @Override
    public void run() {
        int count1 = 0;
        int x =0;
//
        for (int j = maxTicket; j > 0; j-=totalTickets) {

            if (j >= totalTickets) {
                System.out.println(totalTickets +" Max ticket value add to the Total ticket");
                System.out.println("Now total ticket value: "+ totalTickets);
                x = ++count1;
                System.out.println("Remaining Max ticket value: "+ (maxTicket-(totalTickets*x)));
            }
            else {
                System.out.println((maxTicket-(totalTickets*x))+" Remaining Max ticket value add to the Total ticket");
                System.out.println("add remain total ticket value: "+ (maxTicket-(totalTickets*x)));
                totalTickets=maxTicket-(totalTickets*x);
                System.out.println("Remaining Max ticket value: "+(maxTicket-((totalTickets*x)+maxTicket-(totalTickets*x))));
            }

            System.out.println();

        int y = 0;
        int count2=0;
            for (int i = totalTickets; i >0; i-=ticketReleaseRate) {// Vendor adds tickets 5 times
                if(i>=ticketReleaseRate){
                    passTicket.addTickets(ticketReleaseRate);// Adds 2 tickets each time
                    y = ++count2;
                }
                else {
                    passTicket.addTickets(totalTickets -ticketReleaseRate*y);
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

