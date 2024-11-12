// OrderRepository.java
package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "Orders")
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o.orderId FROM Order o WHERE o.tablee.tableId = :tableId ORDER BY o.createdDate DESC")
    Long findLatestOrderIdByTableId(@Param("tableId") Long tableId, PageRequest pageRequest);

}
