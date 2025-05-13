package com.backend.backend.controller;

import com.backend.backend.entity.Customer;
import com.backend.backend.repo.CustomerRepo;
import com.backend.backend.service.impl.MailApp;
import com.backend.backend.service.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customerstest")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    private final CustomerRepo customerRepo;
    private final CustomerServiceImpl customerService;
    private final MailApp mailApp;

    @Autowired
    public CustomerController(CustomerRepo customerRepo,
                              CustomerServiceImpl customerService,
                              MailApp mailApp) {
        this.customerRepo = customerRepo;
        this.customerService = customerService;
        this.mailApp = mailApp;
    }

    @PostMapping("/configure")
    public String saveCustomer(@RequestBody Customer saveRequestDTO) {
        String message = customerService.saveCustomers(saveRequestDTO);

        mailApp.runApp(
                saveRequestDTO.getTotalTicket(),
                saveRequestDTO.getTicketReleaseRate(),
                saveRequestDTO.getCustomerRetrievalRate(),
                saveRequestDTO.getMaximumNumberOfTickets()
        );
        return message;
    }
}
