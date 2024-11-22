package com.fpoly.be_wanren_buffet.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.OrderRepository;
import com.fpoly.be_wanren_buffet.dao.ReviewRepository;
import com.fpoly.be_wanren_buffet.dto.ReviewDTO;
import com.fpoly.be_wanren_buffet.entity.Order;
import com.fpoly.be_wanren_buffet.entity.Review;

@RestController
@RequestMapping("/api/review")
public class ReviewRestController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @PostMapping("/Creact_review")
    public ResponseEntity<?> submitReview(@RequestBody ReviewDTO reviewDTO) {
        Long orderId = reviewDTO.getOrder();
        String content = reviewDTO.getContent();

        if (orderId == null) {
            return ResponseEntity.badRequest().body("Order ID must not be null");
        }

        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (!optionalOrder.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
        }


        Review review = new Review();
        review.setOrder(optionalOrder.get());
        review.setContent(content);

        reviewRepository.save(review);
        return ResponseEntity.ok("Review submitted successfully");
    }


}
