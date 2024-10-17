package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Role")
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
