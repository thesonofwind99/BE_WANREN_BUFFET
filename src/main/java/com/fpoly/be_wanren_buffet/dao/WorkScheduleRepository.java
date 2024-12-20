package com.fpoly.be_wanren_buffet.dao;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.WorkSchedule;
import com.fpoly.be_wanren_buffet.entity.WorkShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
    /**
     * Tìm WorkSchedule theo User và ngày làm việc
     * @param workDate Ngày làm việc (Date)
     * @return Optional WorkSchedule nếu tìm thấy
     */
    List<WorkSchedule> findByWorkDate(Date workDate);
    boolean existsByUserAndShiftAndWorkDate(User user, WorkShift workShift, Date workDate);

}
