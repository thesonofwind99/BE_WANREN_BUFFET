// OrderRepository.java
package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.enums.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT DISTINCT o FROM Order o " +
            "JOIN FETCH o.orderDetails od " +
            "JOIN FETCH od.product p " +
            "WHERE o.customer.customerId = :customerId " +
            "AND o.orderStatus = :orderStatus")
    List<Order> findPreparingOrdersByCustomerId(@Param("customerId") Long customerId,
                                                @Param("orderStatus") OrderStatus orderStatus);
}
