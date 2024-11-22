package com.fpoly.be_wanren_buffet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fpoly.be_wanren_buffet.entity.Customer;

@RepositoryRestResource(path = "Customer")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Customer> findByEmail(String email);
    Customer findByUsername(String username);

    Customer findByPhoneNumber(String phoneNumber);
}
