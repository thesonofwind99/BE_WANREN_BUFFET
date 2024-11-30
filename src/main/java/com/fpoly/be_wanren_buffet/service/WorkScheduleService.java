package com.fpoly.be_wanren_buffet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.WorkShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dao.WorkScheduleRepository;
import com.fpoly.be_wanren_buffet.dto.WorkShiftDTO;
import com.fpoly.be_wanren_buffet.entity.WorkSchedule;


@Service
public class
WorkScheduleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkScheduleRepository workScheduleRepository;
    public List<WorkShiftDTO> getMonthlyWorkSchedules(int month, int year) {
        List<Object[]> results = userRepository.getWorkSchedulesForMonthAndYear(month, year);
        return results.stream()
                .map(row -> new WorkShiftDTO(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        ((Number) row[4]).longValue()
                ))
                .collect(Collectors.toList());
    }
    public WorkSchedule save(WorkSchedule workSchedule) {
        return workScheduleRepository.save(workSchedule);
    }
    public List<WorkSchedule> getSchedulesByDate(Date workDate) {
        return workScheduleRepository.findByWorkDate(workDate);
    }
    public Optional<WorkSchedule> findById(Long id) {
        return workScheduleRepository.findById(id);
    }
    public boolean existsByUserAndShiftAndWorkDate(User user, WorkShift workShift, Date workDate) {
        return workScheduleRepository.existsByUserAndShiftAndWorkDate(user, workShift, workDate);
    }

}
