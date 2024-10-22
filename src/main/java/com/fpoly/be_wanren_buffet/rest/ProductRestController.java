package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductRestController {
    @Autowired
    ProductRepository  productRepository;

    @GetMapping("/ProductHot")
    public List<Long> getProductsHot() {
        return productRepository.findMostOrderedProduct(PageRequest.of(0, 4));
    }
}
