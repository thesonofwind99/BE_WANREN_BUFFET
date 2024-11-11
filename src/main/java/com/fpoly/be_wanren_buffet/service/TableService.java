package com.fpoly.be_wanren_buffet.service;

import com.fpoly.be_wanren_buffet.dao.TableRepository;
import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
    @Autowired
    private TableRepository tableRepository;
    @Transactional
    public Tablee updateTableStatus(Long tableId, TableStatus status) {
        Tablee table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Table not found with ID: " + tableId));
        table.setTableStatus(status);
        return tableRepository.save(table);
    }
}