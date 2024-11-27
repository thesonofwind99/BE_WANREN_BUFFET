package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dto.HourlyRevenueDTO;
import com.fpoly.be_wanren_buffet.dto.WeeklyRevenueDTO;
import com.fpoly.be_wanren_buffet.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/statistical")
public class StatisticalRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/monthly-revenue")
    public ResponseEntity<?> getMonthlyRevenue(
            @RequestParam(value = "year", required = false) Integer year) {

        try {
            // Nếu không cung cấp năm, sử dụng năm hiện tại
            if (year == null) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
            }

            // Kiểm tra tính hợp lệ của năm
            if (year < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Năm không hợp lệ.");
            }

            Map<Integer, Double> revenue = orderService.getMonthlyRevenue(year);
            return ResponseEntity.ok(revenue.values());
        } catch (Exception e) {
            // Ghi log lỗi nếu cần thiết
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Có lỗi xảy ra khi lấy doanh thu hàng tháng.");
        }
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<Double>> getWeeklyRevenue() {
        List<WeeklyRevenueDTO> weeklyRevenues = orderService.getWeeklyRevenue();

        // Lấy danh sách dailyRevenue từ weeklyRevenues
        List<Double> dailyRevenues = weeklyRevenues.stream()
                .map(WeeklyRevenueDTO::getDailyRevenue)
                .toList();

        return ResponseEntity.ok(dailyRevenues);
    }

    @GetMapping("/hourly")
    public ResponseEntity<List<Double>> getHourlyRevenue() {
        List<HourlyRevenueDTO> weeklyRevenues = orderService.getHourlyRevenue();

        // Lấy danh sách dailyRevenue từ weeklyRevenues
        List<Double> dailyRevenues = weeklyRevenues.stream()
                .map(HourlyRevenueDTO::getHourlyRevenue)
                .toList();

        return ResponseEntity.ok(dailyRevenues);
    }



}
