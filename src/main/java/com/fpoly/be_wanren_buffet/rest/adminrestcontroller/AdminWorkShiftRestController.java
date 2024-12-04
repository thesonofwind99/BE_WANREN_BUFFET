package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dto.WorkShiftDTO;
import com.fpoly.be_wanren_buffet.service.UserService;
import com.fpoly.be_wanren_buffet.service.WorkScheduleService;
import com.fpoly.be_wanren_buffet.service.WorkShiftService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/work-shift")
public class AdminWorkShiftRestController {
    @Autowired
    private WorkScheduleService workScheduleService;
    @Autowired
    private UserService userService;
    @Autowired
    private WorkShiftService workShiftService;
    @GetMapping("")
    public ResponseEntity<?> getWorkSchedules(
            @RequestParam(required = true) int month,
            @RequestParam(required = true) int year) {
        try {
            log.info("Fetching work schedules for month: {} and year: {}", month, year);

            // Gọi service để lấy danh sách lịch làm việc
            List<WorkShiftDTO> schedules = workScheduleService.getMonthlyWorkSchedules(month, year);

            // Xử lý trường hợp không có dữ liệu
            if (schedules.isEmpty()) {
                log.info("No work schedules found for month: {} and year: {}", month, year);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No work schedules found");
            }

            return ResponseEntity.ok(schedules);

        } catch (IllegalArgumentException e) {
            log.error("Invalid arguments: month = {}, year = {}", month, year, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request parameters");
        } catch (Exception e) {
            log.error("Unexpected error fetching work schedules", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching work schedules");
        }
    }

}
