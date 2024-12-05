// OrderRepository.java
package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.dto.WeeklyRevenueDTO;
import com.fpoly.be_wanren_buffet.entity.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    @Procedure(name = "getWeeklyRevenue")
    List<Object[]> getWeeklyRevenue();

    @Procedure(procedureName = "GetHourlyRevenue")
    List<Object[]> getHourlyRevenue();

    Order findByOrderId(Long orderId);


    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.customer.customerId = :customerId WHERE o.orderId = :orderId")
    void updateCustomerIdByOrderId(@Param("customerId") Long customerId, @Param("orderId") Long orderId);

    @Query("SELECT CASE WHEN o.customer IS NOT NULL THEN TRUE ELSE FALSE END FROM Order o WHERE o.orderId = :orderId")
    boolean existsCustomerByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT o.discountPointUsed FROM Order o WHERE o.orderId = :orderId")
    Optional<Long> findDiscountPointsByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Query("UPDATE Order o SET o.discountPointUsed = :discountPoints WHERE o.orderId = :orderId")
    int updateDiscountPointsByOrderId(@Param("discountPoints") Long discountPoints, @Param("orderId") Long orderId);





}
