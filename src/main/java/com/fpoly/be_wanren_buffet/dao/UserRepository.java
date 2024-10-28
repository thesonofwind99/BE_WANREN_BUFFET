package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "User")
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String username);

}
