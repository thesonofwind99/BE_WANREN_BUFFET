package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "Role")
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r.roleName FROM UserRole ur JOIN ur.role r WHERE ur.user.userId = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") Long userId);
}
