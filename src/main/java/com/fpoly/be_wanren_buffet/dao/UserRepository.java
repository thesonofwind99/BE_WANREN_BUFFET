package com.fpoly.be_wanren_buffet.dao;


import com.fpoly.be_wanren_buffet.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "User")
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm kiếm theo username, trả về Optional để kiểm tra tồn tại dễ dàng hơn
    Optional<User> findByUsername(String username);

    // Tìm kiếm theo email, trả về Optional để kiểm tra tồn tại dễ dàng hơn
    Optional<User> findByEmail(String email);

    // Tìm kiếm người dùng có tên chứa một chuỗi ký tự (không phân biệt hoa thường)
    Page<User> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // Tìm kiếm người dùng có username chứa một chuỗi ký tự (không phân biệt hoa thường)
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

    // Tìm kiếm người dùng có email chứa một chuỗi ký tự (không phân biệt hoa thường)
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    @Query(value = """
    SELECT 
        u.user_id AS userId,
        u.username AS username,
        u.fullname AS fullName,
        u.user_type AS userType,
        SUM(TIMESTAMPDIFF(HOUR, wsh.start_time, wsh.end_time)) AS totalHours
    FROM user u
    JOIN work_schedule ws ON u.user_id = ws.user_id
    JOIN work_shift wsh ON ws.shift_id = wsh.shift_id
    WHERE MONTH(ws.work_date) = :month
      AND YEAR(ws.work_date) = :year
    GROUP BY u.user_id, u.username, u.fullname, u.user_type
    """, nativeQuery = true)
    List<Object[]> getWorkSchedulesForMonthAndYear(@Param("month") int month, @Param("year") int year);

}
