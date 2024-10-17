package com.fpoly.be_wanren_buffet.dao;

import com.fpoly.be_wanren_buffet.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "Work_schedule")
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
}
