package com.fpoly.be_wanren_buffet.service;


import com.fpoly.be_wanren_buffet.dao.WorkShiftRepository;
import com.fpoly.be_wanren_buffet.entity.WorkShift;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WorkShiftService {
    @Autowired
    private WorkShiftRepository workShiftRepository;

    public Optional<WorkShift> findById(Long shiftId) {
        return workShiftRepository.findById(shiftId);
    }
}
