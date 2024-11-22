package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Work_shift")
public interface WorkShiftRepository extends JpaRepository<WorkShift, Long> {

}
