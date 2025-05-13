package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Configure {
    public static void main(String[] args) {
        System.out.println("Welcome to Ticket Management...!\n");

        Scanner sc = new Scanner(System.in);

        // Input total ticket
        int total_ticket = 0;
        while (true) {
            try {
                System.out.print("Enter Total Number of Tickets: ");
                total_ticket = sc.nextInt();

                if (total_ticket >= 0) {
                    break;
                } else {
                    throw new ArithmeticException();
                }
            } catch (ArithmeticException e) {
                System.out.println("Enter a positive number.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                sc.nextLine(); // Clear the invalid input
            }
        }

        // Input ticket release rate
        int ticketReleaseRate = 0;
        while (true) {
            try {
                System.out.print("Enter Ticket Release Rate: ");
                ticketReleaseRate = sc.nextInt();

                if (ticketReleaseRate >= 0) {
                    break;
                } else {
                    throw new ArithmeticException();
                }
            } catch (ArithmeticException e) {
                System.out.println("Enter a positive number.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                sc.nextLine(); // Clear the invalid input
            }
        }

        // Ticket retrieval rate
        int customerRetrievalRate = 0;
        while (true) {
            try {
                System.out.print("Enter Ticket Retrieval Rate: ");
                customerRetrievalRate = sc.nextInt();

                if (customerRetrievalRate >= 0) {
                    break;
                } else {
                    throw new ArithmeticException();
                }
            } catch (ArithmeticException e) {
                System.out.println("Enter a positive number.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                sc.nextLine(); // Clear the invalid input
            }
        }

        // Enter maximum ticket capacity
        int maximumNumberOfTickets = 0;
        while (true) {
            try {
                System.out.print("Enter Maximum Ticket Capacity: ");
                maximumNumberOfTickets = sc.nextInt();

                if (total_ticket <= maximumNumberOfTickets) {
                    break;
                } else {
                    throw new ArithmeticException();
                }
            } catch (ArithmeticException e) {
                System.out.println("Maximum tickets must be greater than total tickets and greater than 0.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                sc.nextLine();
            }
        }

        System.out.println();
        System.out.println("Total Tickets: " + total_ticket);
        System.out.println("Ticket Release Rate: " + ticketReleaseRate);
        System.out.println("Ticket Retrieval Rate: " + customerRetrievalRate);
        System.out.println("Maximum Ticket Capacity: " + maximumNumberOfTickets);

        System.out.println();
        System.out.println("--------SYSTEM STARTED--------\n");

        // Create a configuration object
        Configuration config = new Configuration(
                total_ticket, ticketReleaseRate, customerRetrievalRate, maximumNumberOfTickets);

        // Save to JSON file
        saveToJson(config);

        // Pass the total_ticket value to MailApp
        MailApp.runApp(total_ticket, ticketReleaseRate, customerRetrievalRate, maximumNumberOfTickets);
    }

    // Method to save the configuration to a JSON file
    private static void saveToJson(Configuration config) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("ticketConfig.json"), config);
            System.out.println("Configuration saved to ticketConfig.json");
        } catch (IOException e) {
            System.out.println("Failed to save configuration to JSON.");
            e.printStackTrace();
        }
    }
}

// Configuration class to hold ticket settings
class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTickets;

    // Constructor
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTickets) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTickets = maxTickets;
    }

    // Getters and setters
    @JsonProperty
    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    @JsonProperty
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @JsonProperty
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @JsonProperty
    public int getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
    }
}


