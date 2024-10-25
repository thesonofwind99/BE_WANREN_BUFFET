package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.entity.Product;
import com.fpoly.be_wanren_buffet.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductRestController {
    @Autowired
    private ProductService productService;

    @CrossOrigin("http://localhost:3000")
    @GetMapping("/ProductHot")
    public List<Long> getProductsHot() {
        return productService.findAllProductsHot();
    }
}
