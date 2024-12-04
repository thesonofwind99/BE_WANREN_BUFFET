package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.Tablee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;


@RepositoryRestResource(path = "Table")
public interface TableRepository extends JpaRepository<Tablee, Long> {
}
