package com.backend.backend.service.impl;

import com.backend.backend.entity.Customer;
import com.backend.backend.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public String saveCustomers(Customer saveRequestDTO) {
        Customer customer = new Customer(
                saveRequestDTO.getId(),
                saveRequestDTO.getCustomerName(),
                saveRequestDTO.getTotalTicket(),
                saveRequestDTO.getTicketReleaseRate(),
                saveRequestDTO.getCustomerRetrievalRate(),
                saveRequestDTO.getMaximumNumberOfTickets()
        );

        customerRepo.save(customer);

        return "Customer saved successfully!";
    }
}
