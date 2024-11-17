package com.fpoly.be_wanren_buffet.dao;
import com.fpoly.be_wanren_buffet.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByRoleRoleId(Long roleId);
    List<UserRole> findByUserUserId(Long userId);
    boolean existsByUserUserIdAndRoleRoleId(Long userId, Long roleId);
}