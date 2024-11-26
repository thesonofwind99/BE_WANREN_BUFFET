// OrderRepository.java
package com.fpoly.be_wanren_buffet.dao;

import java.util.List;

import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.fpoly.be_wanren_buffet.entity.Order;

@RepositoryRestResource(path = "Orders")
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.orderId FROM Order o WHERE o.tablee.tableId = :tableId ORDER BY o.createdDate DESC")
    Long findLatestOrderIdByTableId(@Param("tableId") Long tableId, PageRequest pageRequest);

    @Query("SELECT " +
            "MONTH(o.createdDate) AS month, " +
            "SUM(o.totalAmount) AS revenue " +
            "FROM Order o " +
            "WHERE YEAR(o.createdDate) = :year " +
            "AND o.orderStatus = 'DELIVERED' " +
            "GROUP BY MONTH(o.createdDate) " +
            "ORDER BY MONTH(o.createdDate)")
    List<Object[]> getMonthlyRevenue(@Param("year") int year);

    Order findByOrderId(Long orderId);

}
