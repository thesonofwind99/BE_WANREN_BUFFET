package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "Order_detail")
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

}
