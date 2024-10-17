package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Auditable;
import com.fpoly.be_wanren_buffet.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Customer")
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
