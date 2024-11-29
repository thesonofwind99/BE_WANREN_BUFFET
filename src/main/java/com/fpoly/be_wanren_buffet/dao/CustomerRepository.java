package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.dto.UpdateCustomerDTO;
import com.fpoly.be_wanren_buffet.entity.Auditable;
import com.fpoly.be_wanren_buffet.entity.Customer;
import com.fpoly.be_wanren_buffet.entity.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "Customer")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByUsername(String username);

   Customer findByPhoneNumber(String phoneNumber);

    Page<Customer> findByFullNameContaining(String fullName, Pageable pageable);

    @Query("select u from Customer u where u.customerId = :customerId")
    Optional<Customer> findCustomerByCustomerId(@Param("customerId") Long customerId);

}
