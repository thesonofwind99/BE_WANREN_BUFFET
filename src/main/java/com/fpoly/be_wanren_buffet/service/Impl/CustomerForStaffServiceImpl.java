package com.fpoly.be_wanren_buffet.service.Impl;

import com.fpoly.be_wanren_buffet.dao.CustomerRepository;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.service.CustomerForStaffService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerForStaffServiceImpl implements CustomerForStaffService {
    private final CustomerRepository customerRepository;

    @Override
    public Long updateloyalPointOfCustomer(String phoneNumber, Double totalAmount) {
        Customer customer = customerRepository.findByPhoneNumber(phoneNumber);
        double fivePercent = (totalAmount * 5) / 100;
        customer.setLoyaltyPoints(customer.getLoyaltyPoints() + Math.round(fivePercent));
        Customer customer1 = customerRepository.save(customer);
        return customer1.getLoyaltyPoints();
    }
}
