package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Payment")
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
