package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.dto.UpdateCustomerDTO;
import com.fpoly.be_wanren_buffet.entity.Auditable;
import com.fpoly.be_wanren_buffet.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "Customer")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
    Customer findByUsername(String username);

    Customer findByPhoneNumber(String phoneNumber);
}
