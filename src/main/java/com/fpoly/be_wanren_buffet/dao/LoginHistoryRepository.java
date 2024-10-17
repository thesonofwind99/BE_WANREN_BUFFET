package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Login_history")
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
}
