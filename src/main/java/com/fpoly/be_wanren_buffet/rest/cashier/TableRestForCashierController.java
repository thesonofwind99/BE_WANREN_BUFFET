package com.fpoly.be_wanren_buffet.rest.cashier;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.be_wanren_buffet.dao.TableRepository;
import com.fpoly.be_wanren_buffet.dto.TableDTO;
import com.fpoly.be_wanren_buffet.entity.Tablee;

@RestController
@RequestMapping("/Table")
public class TableRestForCashierController {

    @Autowired
    private TableRepository tableRepository;

    @GetMapping("/all")
    public List<TableDTO> getAllTables() {
        System.out.println("test how many this run");
        List<Tablee> tables = tableRepository.findAll();
        return tables.stream()
                .map(table -> new TableDTO(table.getTableId(), table.getTableNumber(), table.getTableStatus(),
                        table.getLocation()))
                .collect(Collectors.toList());
    }
}
