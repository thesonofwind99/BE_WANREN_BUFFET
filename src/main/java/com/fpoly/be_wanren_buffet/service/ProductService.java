package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.ProductRepository;
import com.fpoly.be_wanren_buffet.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Long> findAllProductsHot() {
      return   productRepository.findMostOrderedProduct(PageRequest.of(0, 4));
    }
}
