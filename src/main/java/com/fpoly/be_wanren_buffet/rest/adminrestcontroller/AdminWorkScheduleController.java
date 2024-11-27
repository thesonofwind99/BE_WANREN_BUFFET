package com.fpoly.be_wanren_buffet.rest.adminrestcontroller;

import com.fpoly.be_wanren_buffet.dao.UserRepository;
import com.fpoly.be_wanren_buffet.dto.WorkScheduleDTO;
import com.fpoly.be_wanren_buffet.dto.WorkScheduleFullDTO;
import com.fpoly.be_wanren_buffet.entity.User;
import com.fpoly.be_wanren_buffet.entity.WorkSchedule;
import com.fpoly.be_wanren_buffet.entity.WorkShift;
import com.fpoly.be_wanren_buffet.service.WorkScheduleService;
import com.fpoly.be_wanren_buffet.service.WorkShiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/work-schedules")
public class AdminWorkScheduleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkShiftService workShiftService;

    @Autowired
    private WorkScheduleService workScheduleService;

    @PostMapping("/create")
    public ResponseEntity<?> createWorkSchedule(@RequestBody WorkScheduleDTO workScheduleDTO) {
        try {
            // Tìm User theo username
            User user = userRepository.findByUsername(workScheduleDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            // Tìm WorkShift theo shiftId
            WorkShift workShift = workShiftService.findById(workScheduleDTO.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Shift không tồn tại"));

            // Tạo WorkSchedule entity
            WorkSchedule workSchedule = new WorkSchedule();
            workSchedule.setUser(user);
            workSchedule.setShift(workShift);
            workSchedule.setWorkDate(new SimpleDateFormat("yyyy-MM-dd").parse(workScheduleDTO.getWorkDate()));

            // Lưu WorkSchedule
            WorkSchedule savedWorkSchedule = workScheduleService.save(workSchedule);

            // Tạo WorkScheduleFullDTO để trả về
            WorkScheduleFullDTO response = new WorkScheduleFullDTO(
                    user.getUsername(),
                    user.getFullName(),
                    user.getUserType(),
                    workShift.getShiftId(),
                    workShift.getShiftName(),
                    savedWorkSchedule.getWorkDate()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Lỗi khi tạo WorkSchedule", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/{date}")
    public ResponseEntity<?> getWorkSchedulesByDate(@PathVariable("date") String date) {
        try {
            Date workDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            List<WorkSchedule> workSchedules = workScheduleService.getSchedulesByDate(workDate);

            List<WorkScheduleFullDTO> response = workSchedules.stream().map(workSchedule -> {
                User user = workSchedule.getUser();
                WorkShift workShift = workSchedule.getShift();

                return new WorkScheduleFullDTO(
                        user.getUsername(),
                        user.getFullName(),
                        user.getUserType(),
                        workShift.getShiftId(),
                        workShift.getShiftName(),
                        workSchedule.getWorkDate()
                );
            }).collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error retrieving WorkSchedules", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
